package com.ajay.ailms.dto;

import com.ajay.ailms.entity.Question;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
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
