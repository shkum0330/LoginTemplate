package com.example.login.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request,"Access");
        String refreshToken = jwtTokenProvider.resolveToken(request,"Refresh");
        log.info("{}",accessToken);
        if(accessToken != null) {
            // 액세스 토큰이 유효하다면 setAuthentication를 통해 security context에 인증 정보저장
            String isLogout = (String)redisTemplate.opsForValue().get(accessToken);
            if(jwtTokenProvider.validateToken(accessToken) && ObjectUtils.isEmpty(isLogout) ){
                setAuthentication(jwtTokenProvider.getUserId(accessToken));
                log.info("info1 {}",isLogout);
            }
            // 액세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
            else if (refreshToken != null) {
                // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
                boolean isRefreshToken = jwtTokenProvider.validateRefreshToken(refreshToken);
                // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
                if (isRefreshToken) {
                    // 리프레시 토큰으로 아이디 정보 가져오기
                    String loginId = jwtTokenProvider.getUserId(refreshToken);

                    // 새로운 access 토큰 발급
                    String newAccessToken = jwtTokenProvider.createToken(loginId, "Access");
                    // 헤더에 access 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                    // Security context에 인증 정보 넣기
                    setAuthentication(jwtTokenProvider.getUserId(newAccessToken));
                }
                // 리프레시 토큰이 만료 or 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
                else {
                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }
        log.info("last {}",accessToken);
        filterChain.doFilter(request,response);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String userId) {
        Authentication authentication = jwtTokenProvider.getAuthentication(userId);
        // security가 만들어주는 securityContextHolder 그 안에 authentication을 넣어줍니다.
        // security가 securitycontextholder에서 인증 객체를 확인하는데
        // jwtAuthfilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않습니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Jwt 예외처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(msg+" "+status.value());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
