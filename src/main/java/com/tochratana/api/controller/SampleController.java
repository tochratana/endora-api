package com.tochratana.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SampleController {

    // Sample endpoint: List all users
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = List.of(
                Map.of("id", 1, "name", "John Doe", "email", "john@example.com"),
                Map.of("id", 2, "name", "Jane Smith", "email", "jane@example.com")
        );
        return ResponseEntity.ok(users);
    }

    // Sample endpoint: List all products (like product cards)
    @GetMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> getAllProducts() {
        List<Map<String, Object>> products = List.of(
                Map.of("id", 1, "name", "Laptop", "price", 999.99, "imageUrl", "/images/laptop.png"),
                Map.of("id", 2, "name", "Smartphone", "price", 699.99, "imageUrl", "/images/smartphone.png")
        );
        return ResponseEntity.ok(products);
    }
}
