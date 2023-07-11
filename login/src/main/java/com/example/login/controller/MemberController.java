package com.example.login.controller;

import com.example.login.jwt.JwtTokenProvider;
import com.example.login.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

}
