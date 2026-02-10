package com.ajay.ailms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,max = 6)
    private String otp;

    @Size(min=8)
    @NotBlank
    private String newPassword;
}
