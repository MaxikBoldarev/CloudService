package ru.netology.utils;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.netology.auth.JwtAuth;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuth generate(Claims claims) {
        final JwtAuth jwtInfoToken = new JwtAuth();
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }
}