package com.example.controller;

import com.example.dto.ForgotPasswordRequest;
import com.example.dto.JwtResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.ResetPasswordRequest;
import com.example.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public String forgot(@RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request.getEmail());
    }

    @PostMapping("/reset-password")
    public String reset(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(
            request.getEmail(),
            request.getOtp(),
            request.getNewPassword()
        );
    }
}