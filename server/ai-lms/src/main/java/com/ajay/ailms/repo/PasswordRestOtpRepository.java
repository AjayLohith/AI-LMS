package com.ajay.ailms.repo;

import com.ajay.ailms.entity.PasswordRestOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRestOtpRepository extends JpaRepository<PasswordRestOtp, Long> {
    Optional<PasswordRestOtp>findTopByEmailAndOtpAndUsedFalseOrderByIdDesc(
            String email,String otp
    );
    void deleteByEmail(String email);
}