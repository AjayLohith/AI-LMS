package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerDto {
    private Long questionId;
    private String selectedOption;
}

