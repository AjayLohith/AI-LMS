package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private Integer passingScore;
    private Integer timeLimitInMins;
    private LocalDateTime createdAt;
    private Set<QuestionDto> questions = new HashSet<>();
    private Integer totalQuestions;

}
