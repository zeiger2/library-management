package com.artzver.library.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Логируем информацию о запросе ДО обработки
        System.out.println("=== BEFORE REQUEST ===");
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("Session ID: " + request.getSession().getId());
        System.out.println("Method: " + request.getMethod());

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);

        // Логируем информацию ПОСЛЕ обработки
        System.out.println("=== AFTER REQUEST ===");
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("=================================");
    }
}