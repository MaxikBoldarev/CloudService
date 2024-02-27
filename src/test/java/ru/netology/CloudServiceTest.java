package ru.netology;


import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.netology.controller.CloudController;
import ru.netology.model.JwtRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    private static final String WRONG_LOGIN = "user5";
    private static final String LOGIN = "user1@gmail.com";
    private static final String PASSWORD = "password";
    private static final String ENDPOINT_LOGIN = "/login";
    private static final String ENDPOINT_LOGOUT = "/logout";
    private static JwtRequest validRequest;
    private static JwtRequest invalidRequest;
    private static final Gson gson = new Gson();

    @BeforeAll
    public static void beforeAll() {
        validRequest = new JwtRequest(LOGIN, PASSWORD);
        invalidRequest = new JwtRequest(WRONG_LOGIN, PASSWORD);
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().isOk());
    }

    @Test
    void testLoginFail() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(invalidRequest))).andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGOUT).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().is3xxRedirection());
    }


    @Test
    void FilesControllerTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertNotNull(webApplicationContext.getBean(CloudController.class));
    }

}
