package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResultDto {
    private Long quizId;
    private int totalQuestions;
    private int correctOptions;
    private double scorePercentage;
    private boolean passed;
}
