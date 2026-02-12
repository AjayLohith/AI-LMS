package com.ajay.ailms.controller;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.Course;
import com.ajay.ailms.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('INSTRUCTOR')")
@RequestMapping("/api/v1/instructor/")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping("/create-courses")
    public ResponseEntity<CourseDto>createCourse(CreateCourseDto dto){
        return ResponseEntity.ok(instructorService.createCourse(dto));
    }

    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<LessonDto>createLesson(
            @PathVariable Long courseId,
            @RequestBody CreateLessonDto dto){
        return ResponseEntity.ok(instructorService.createLesson(dto));
    }

    @PostMapping("/lessons/{lessopnId}/quiz")
    public ResponseEntity<QuizDto>createQuiz(
            @PathVariable Long lessonId,
            @RequestBody CreateQuizDto dto
    ){
        return ResponseEntity.ok(instructorService.createQuiz(lessonId,dto));
    }

    @PostMapping("/quiz/{quizId}/questions")
    public ResponseEntity<QuestionDto>createQuestion(
            @PathVariable Long quizId,
            @RequestBody CreateQuestionDto dto
    ){
        return ResponseEntity.ok(instructorService.createQuestion(dto));
    }



}
