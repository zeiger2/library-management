package com.artzver.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", request.getSession().getId());
        response.put("sessionCreated", request.getSession().getCreationTime());
        response.put("isNew", request.getSession().isNew());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/books")
    public ResponseEntity<?> getBooksTest() {
        List<Map<String, String>> books = List.of(
                Map.of("id", "1", "title", "Test Book 1", "author", "Author 1"),
                Map.of("id", "2", "title", "Test Book 2", "author", "Author 2")
        );
        return ResponseEntity.ok(books);
    }
}