package com.ajay.ailms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuizDto {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer passingScore;

    private Integer timeLimitInMinutes;
}

