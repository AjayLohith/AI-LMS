package com.ajay.ailms.controller;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.PasswordRestOtp;
import com.ajay.ailms.service.AuthService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }


    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto>signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto
            ){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?>forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto
            ){
        authService.sendPasswordResetOtp(forgotPasswordRequestDto.getEmail());
        return ResponseEntity.ok("Otp sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String>resetPassword(
            @Valid @RequestBody PasswordResetDto passwordRequestDto
    ){
        authService.resetPassword(passwordRequestDto);
        return ResponseEntity.ok("Password Reset Successfully");
    }

}
