package com.example.service;

import com.example.dto.*;

public interface AuthService {

    String register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    String forgotPassword(String email);

    String resetPassword(String email, String otp, String newPassword);
}