package com.example.login;

import com.example.login.member.Member;
import com.example.login.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member1 = new Member();
            member1.setEmail("kumsh0330@naver.com");
            member1.setPassword("1234");
            member1.setNickname("sehyun");
            member1.setRole(Role.USER);
            em.persist(member1);
        }

        public void dbInit2() {
            Member member2 = new Member();
            member2.setEmail("shkum0330@gmail.com");
            member2.setPassword("1234");
            member2.setNickname("sh");
            member2.setRole(Role.ADMIN);
            em.persist(member2);
        }
    }
}
