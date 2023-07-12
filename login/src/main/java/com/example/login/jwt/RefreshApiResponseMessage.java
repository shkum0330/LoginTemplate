package com.example.login.jwt;

import java.util.Map;

public class RefreshApiResponseMessage {
    Map<String, String> map;

    public RefreshApiResponseMessage(Map<String, String> map) {
        this.map = map;
    }
}
