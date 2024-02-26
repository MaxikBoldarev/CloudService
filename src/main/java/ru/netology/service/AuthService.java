package ru.netology.service;

import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.model.JwtRequest;
import ru.netology.model.JwtResponse;
import ru.netology.providers.JwtProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class  AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;


    @Transactional
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {

        final String username = authRequest.getLogin();
        final String password = authRequest.getPassword();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtProvider.generateToken(userDetails);
        log.info("Произошла авторицазия");
        return new JwtResponse(token);
    }


}