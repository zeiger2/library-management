package com.artzver.library.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("=== Генератор паролей ===");
        System.out.println("Исходный пароль: " + rawPassword);
        System.out.println("Захешированный пароль: " + encodedPassword);
        System.out.println("=========================");

        // Проверка что пароль совпадает
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Пароль проверяется: " + matches);
    }
}