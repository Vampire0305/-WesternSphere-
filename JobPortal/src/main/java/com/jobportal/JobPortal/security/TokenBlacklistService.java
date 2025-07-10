package com.jobportal.JobPortal.security;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TokenBlacklistService {

    private final ConcurrentMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklistToken(String token) {
        blacklistedTokens.put(token, System.currentTimeMillis());
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    public void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        long expirationTime = 24 * 60 * 60 * 1000; // 24 hours

        blacklistedTokens.entrySet().removeIf(entry ->
                currentTime - entry.getValue() > expirationTime);
    }
}