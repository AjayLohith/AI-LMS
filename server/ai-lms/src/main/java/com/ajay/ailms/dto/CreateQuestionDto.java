package com.ajay.ailms.dto;

import lombok.Data;

@Data
public class CreateQuestionDto {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
}
