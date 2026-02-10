package com.ajay.ailms.service;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.PasswordRestOtp;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.entity.type.Role;
import com.ajay.ailms.repo.PasswordRestOtpRepository;
import com.ajay.ailms.repo.UserRepository;
import com.ajay.ailms.util.AuthUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordRestOtpRepository otpRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
            )
        );

        User user= (User) authentication.getPrincipal();

        String token =authUtil.generateToken(user);
        return new LoginResponseDto(token,user.getId());
    }


    public SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto) {
        if(userRepo.existsByEmail(signupRequestDto.getEmail())){
            throw new IllegalArgumentException("User already exists");
        }

        User user=userRepo.save(
                User.builder()
                        .email(signupRequestDto.getEmail())
                        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                        .role(Role.STUDENT)
                        .build());
        return new SignupResponseDto(user.getId(),user.getEmail());
    }

    @Transactional
    public void sendPasswordResetOtp(@NotBlank @Email String email) {
        User user=userRepo.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        otpRepo.deleteByEmail(email);

        SecureRandom secureRandom=new SecureRandom();
        int otp=secureRandom.nextInt(900_000)+100_000;
        String otpString=String.valueOf(otp);

        PasswordRestOtp passwordRestOtp=otpRepo.save(PasswordRestOtp.builder()
                .email(email)
                .otp(otpString)
                .used(false)
                .expiryAt(LocalDateTime.now().plusMinutes(5))
                .build()
        );
        emailService.sendOtp(email,otpString);
    }

    @Transactional
    public void resetPassword(@Valid PasswordResetDto passwordResetDto) {
        PasswordRestOtp otp=otpRepo.findTopByEmailAndOtpAndUsedFalseOrderByIdDesc(
                passwordResetDto.getEmail(),
                passwordResetDto.getOtp()).orElseThrow(
                ()->new IllegalArgumentException("Invalid OTP")
        );

        if(otp.getExpiryAt().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("OTP expired");
        }

        User user=userRepo.findByEmail(passwordResetDto.getEmail())
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        userRepo.save(user);
        otp.setUsed(true);
        otpRepo.save(otp);
    }
}
