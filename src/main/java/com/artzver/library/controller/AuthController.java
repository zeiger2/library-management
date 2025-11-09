package com.artzver.library.controller;

import com.artzver.library.dto.LoginRequest;
import com.artzver.library.entity.User;
import com.artzver.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            System.out.println("=== LOGIN START ===");

            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // СОЗДАЕМ НОВЫЙ SecurityContext и устанавливаем аутентификацию
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // СОХРАНЯЕМ SecurityContext в сессии
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            System.out.println("=== LOGIN SUCCESS ===");
            System.out.println("User: " + authentication.getName());
            System.out.println("Session: " + request.getSession().getId());

            // Получение информации о пользователе
            Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

            if (user.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Успешный вход в систему");
                response.put("userId", user.get().getId());
                response.put("email", user.get().getEmail());
                response.put("sessionId", request.getSession().getId());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Пользователь не найден");
            }
        } catch (Exception e) {
            System.out.println("=== LOGIN ERROR ===");
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Неверный email или пароль");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Успешный выход из системы");
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication.isAuthenticated());
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("principal", authentication.getPrincipal().getClass().getSimpleName());

        return ResponseEntity.ok(response);
    }
}