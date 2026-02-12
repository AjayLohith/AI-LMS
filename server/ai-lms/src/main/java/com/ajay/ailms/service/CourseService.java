package com.ajay.ailms.service;

import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.entity.Course;
import com.ajay.ailms.repo.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;


    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepo.findAllWithInstructor(pageable)
                .map(this::mapToDto);
    }

    public Page<CourseDto> getMyEnrolledCourses(Long id,Pageable pageable) {
        return courseRepo.findCoursesByStudentId(id,pageable)
                .map(this::mapToDto);
    }


    private CourseDto mapToDto(Course course){
        CourseDto dto=new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDifficulty(course.getDifficulty());
        dto.setRating(course.getRating());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setInstructorName(course.getInstructor().getUsername());
        return dto;

    }
}
