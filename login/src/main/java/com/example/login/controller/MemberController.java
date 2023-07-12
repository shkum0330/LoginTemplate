package com.example.login.controller;

import com.example.login.jwt.JwtService;
import com.example.login.jwt.JwtTokenProvider;
import com.example.login.jwt.Token;
import com.example.login.member.Member;
import com.example.login.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private JwtService jwtService;
    // 로그인
    @PostMapping("/login")
    public Token login(@RequestBody Map<String, String> memberMap) {
        log.info("user email = {}", memberMap.get("email"));
        Member member = memberRepository.findByUserEmail(memberMap.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        Token tokenDto = jwtTokenProvider.createAccessToken(member.getUsername(), member.getRoles());
        log.info("roles = {}", member.getRoles());
        log.info("token = {}",tokenDto);
        jwtService.login(tokenDto);
        return tokenDto;
    }
}
