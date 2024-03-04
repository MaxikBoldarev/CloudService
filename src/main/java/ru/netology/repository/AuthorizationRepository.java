package ru.netology.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthorizationRepository {

    private final Map<String, String> tokenAndUserNames = new ConcurrentHashMap<>();

    public void putTokenAndUsername(String token, String username) {
        tokenAndUserNames.put(token, username);
    }

    public String getUserNameByToken(String token) {
        return tokenAndUserNames.get(token);
    }

    public void removeTokenAndUsernameByToken(String token) {
        tokenAndUserNames.remove(token);
    }
}
