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
public class SignupRequestDto {
        @NotBlank(message = "Email Required")
        @Email(message = "Invalid email")
        private String email;

        @NotBlank(message = "Password Required")
        @Size(message = "Password must be atleast 6 characters",min = 6)
        private String password;

}


