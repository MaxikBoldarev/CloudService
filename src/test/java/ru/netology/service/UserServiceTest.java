package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.model.User;
import ru.netology.repository.UserRepository;
import ru.netology.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    public static final String LOGIN_1 = "user";
    public static final String PASSWORD_1 = "password";
    public static final User USER_1 = new User(1L, LOGIN_1, PASSWORD_1, null);
    public static final UserDetails USER_DETAILS_1 = org.springframework.security.core.userdetails.User.builder()
            .username(LOGIN_1)
            .password(PASSWORD_1)
            .authorities(new ArrayList<>())
            .build();

    @BeforeEach
    void setUp() {
        Mockito.when(userRepository.findUserByUsername(LOGIN_1)).thenReturn(Optional.of(USER_1));
    }

    @Test
    void loadUserByUsername() {
        Assertions.assertEquals(USER_DETAILS_1, userService.loadUserByUsername(LOGIN_1));
    }
}