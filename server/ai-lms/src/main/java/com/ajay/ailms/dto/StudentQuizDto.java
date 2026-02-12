package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudentQuizDto {
    private Long id;
    private String title;
    private String description;
    private Integer timeLimitInMins;
    private List<QuestionViewDto>questions;
}
