package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuestionViewDto {

    private Long id;

    private String questionText;

    private List<String> options;
}

