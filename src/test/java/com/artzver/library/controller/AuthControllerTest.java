package com.artzver.library.controller;

import com.artzver.library.dto.LoginRequest;
import com.artzver.library.entity.User;
import com.artzver.library.repository.UserRepository;
import com.artzver.library.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@library.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setFirstName("Тест");
        testUser.setLastName("Пользователь");
        testUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(testUser);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@library.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Успешный вход в систему"))
                .andExpect(jsonPath("$.email").value("test@library.com"));
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnError() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@library.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Неверный email или пароль"));
    }

    @Test
    void login_WithNonExistentUser_ShouldReturnError() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent@library.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Неверный email или пароль"));
    }
}