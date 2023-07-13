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
            Member member1 = new Member("kumsh0330@naver.com","1234");
            member1.getRoles().add(Role.USER.name());
            em.persist(member1);
        }

        public void dbInit2() {
            Member member2 = new Member("shkum0330@gmail.com","1234");
            member2.getRoles().add(Role.ADMIN.name());
            em.persist(member2);
        }
    }
}
