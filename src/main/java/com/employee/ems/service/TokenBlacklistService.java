package com.employee.ems.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String token, long expiryTime) {
        blacklist.put(token, expiryTime);
    }

    public boolean isTokenBlacklisted(String token) {
        Long expiry = blacklist.get(token);
        if (expiry == null) return false;

        if (expiry < System.currentTimeMillis()) {
            blacklist.remove(token); // Clean up
            return false;
        }
        return true;
    }
}
