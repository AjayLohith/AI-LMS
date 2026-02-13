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
        return ResponseEntity.ok(instructorService.createLesson(courseId,dto));
    }

    @PostMapping("/lessons/{lessonId}/quiz")
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
        return ResponseEntity.ok(instructorService.createQuestion(quizId,dto));
    }

    @DeleteMapping("/delete-quiz/{quizId}")
    public ResponseEntity<String>deleteQuiz(@PathVariable Long quizId){
        return ResponseEntity.ok(instructorService.deleteQuiz(quizId));
    }

    @DeleteMapping("/delete-question/{questionId}")
    public ResponseEntity<String>deleteQuestion(@PathVariable Long questionId){
        return ResponseEntity.ok(instructorService.deleteQuestion(questionId));
    }

    @DeleteMapping("/delete-course/{courseId}")
    public ResponseEntity<String>deleteCourse(@PathVariable Long courseId){
        return ResponseEntity.ok(instructorService.deleteCourse(courseId));
    }



}
