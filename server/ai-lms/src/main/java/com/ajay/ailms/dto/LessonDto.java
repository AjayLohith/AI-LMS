package com.ajay.ailms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class LessonDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;

}
