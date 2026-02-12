package com.ajay.ailms.service;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.Course;
import com.ajay.ailms.entity.Lesson;
import com.ajay.ailms.entity.Quiz;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.entity.type.Role;
import com.ajay.ailms.repo.CourseRepository;
import com.ajay.ailms.repo.LessonRepository;
import com.ajay.ailms.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorService {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;

//    private Course getInstructorCourse(Long courseId){
//        User instructor=getCurrentInstructor();
//        return courseRepo.
//                findByIdAndInstructor(courseId,instructor)
//                .orElseThrow(()->new RuntimeException("Course not owned by this instructor"));
//    }

    private User getCurrentInstructor() {
        String email= SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user=userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Instructor not found"));

        if(user.getRole() != Role.INSTRUCTOR){
            throw new RuntimeException("Access denied");
        }

        return user;
    }

    public CourseDto createCourse(CreateCourseDto createCourseDto) {
        User instructor=getCurrentInstructor();
        Course course=Course.builder()
                .title(createCourseDto.getTitle())
                .description(createCourseDto.getDescription())
                .difficulty(createCourseDto.getDifficulty())
                .rating(0.0)
                .instructor(instructor)
                .build();
        courseRepo.save(course);
        return mapToDto(course);
    }

    public QuizDto createQuiz(Long lessonId, CreateQuizDto dto) {
        User instructor=getCurrentInstructor();
        Lesson lesson=lessonRepo.findByIdAndCourseInstructor(lessonId,instructor)
                .orElseThrow(()->new RuntimeException("Lesson not owned or found"));


        Quiz quiz= Quiz.builder()
                .title(dto.getTitle())
                .de

                .build()
    }

    public LessonDto createLesson(Long courseId, CreateLessonDto dto) {

        User instructor = getCurrentInstructor();

        Course course = courseRepo
                .findByIdAndInstructor(courseId, instructor)
                .orElseThrow(() -> new RuntimeException("Course not found or not owned"));

        Lesson lesson = Lesson.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .course(course)
                .build();

        lessonRepo.save(lesson);

        return mapToLessonDto(lesson);
    }


    public QuestionDto createQuestion(CreateQuestionDto dto) {
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

    private LessonDto mapToLessonDto(Lesson lesson){
        LessonDto dto=new LessonDto();
        dto.
        return dto;
    }
}
