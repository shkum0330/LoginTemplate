package com.example.login.jwt;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken {
    @Id
    private String refreshToken;
    private String memberId;

    public RefreshToken(final String refreshToken, final String memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getMemberId() {
        return memberId;
    }
}
