package com.codeit.blog.controller;

import com.codeit.blog.dto.user.TestCustomValidRequest;
import com.codeit.blog.dto.user.TestValidRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping
public class ValidationController {

    @GetMapping("/valid/valid-basic")
    public String basicForm() {
        return "valid/valid-basic";
    }

    @GetMapping("/valid/valid-custom")
    public String customForm() {
        return "valid/valid-custom";
    }

    @PostMapping("/api/valid/basic")
    @ResponseBody
    public ResponseEntity<?> validateBasic(@Valid TestValidRequest req) {
        return ResponseEntity.ok(Map.of("message", "OK", "payload", req));
    }

    @PostMapping("/api/valid/custom")
    @ResponseBody
    public ResponseEntity<?> validateCustom(@Valid TestCustomValidRequest req) {
        return ResponseEntity.ok(Map.of("message", "OK", "payload", req));
    }
}
