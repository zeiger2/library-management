package com.artzver.library.service;

import com.artzver.library.entity.User;
import com.artzver.library.repository.UserRepository;
import com.artzver.library.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@library.com");
        testUser.setPassword("password123");
        testUser.setFirstName("Тест");
        testUser.setLastName("Пользователь");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void save_NewUser_ShouldEncodePassword() {
        // Given
        User userToSave = new User();
        userToSave.setEmail("test@library.com");
        userToSave.setPassword("password123");
        userToSave.setFirstName("Тест");
        userToSave.setLastName("Пользователь");

        // Настройка моков ПЕРЕД вызовом
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);  // Устанавливаем ID
            return savedUser;
        });

        // When
        User result = userService.save(userToSave);

        // Then
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("test@library.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findByEmail("test@library.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@library.com", result.get().getEmail());
        verify(userRepository).findByEmail("test@library.com");
    }

    @Test
    void findByEmail_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("nonexistent@library.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findByEmail("nonexistent@library.com");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("nonexistent@library.com");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = userService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("test@library.com", result.get(0).getEmail());
        verify(userRepository).findAll();
    }
}