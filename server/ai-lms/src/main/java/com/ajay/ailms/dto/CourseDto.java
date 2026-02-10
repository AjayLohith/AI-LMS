package com.ajay.ailms.dto;
import com.ajay.ailms.entity.type.Difficulty;
import lombok.Data;

import java.time.LocalDateTime;
@Data

public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private Double rating;
    private LocalDateTime createdAt;

    private Long instructorId;
    private String instructorName;

}
