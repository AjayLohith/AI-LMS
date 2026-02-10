package com.ajay.ailms.controller;

import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.service.CourseService;
import com.ajay.ailms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student/")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class UserController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;


    @GetMapping("/enroll-course")
    public String enroll(){
        return "Enrolled";
    }

    @GetMapping("/{id}/my-courses")
    public  ResponseEntity<Page<CourseDto>> myCourses(@PathVariable Long id,Pageable pageable){
        return ResponseEntity.ok(courseService.getMyEnrolledCourses(id,pageable));
    }

    @GetMapping("/all-courses")
    public ResponseEntity<Page<CourseDto>>getAllCourses(
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC)
            Pageable pageable){
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    @PostMapping("/{courseId}/enroll-course")
    public ResponseEntity<String>enrollCourse(@PathVariable Long courseId){
        return ResponseEntity.ok(enrollmentService.enroll(courseId));
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<Void>completeLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId
    ){
        enrollmentService.completeLesson(courseId,lessonId);
        return ResponseEntity.ok().build();

    }





}
