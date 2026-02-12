package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SubmitQuizDto {
    private Long quizId;
    private List<AnswerDto> answers;
}

