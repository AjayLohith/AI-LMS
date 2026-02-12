package com.ajay.ailms.service;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.*;
import com.ajay.ailms.entity.type.Role;
import com.ajay.ailms.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
@Transactional
public class InstructorService {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;

//

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
        return mapToCourseDto(course);
    }

    public QuizDto createQuiz(Long lessonId, CreateQuizDto dto) {
        User instructor=getCurrentInstructor();
        Lesson lesson=lessonRepo.findByIdAndCourseInstructor(lessonId,instructor)
                .orElseThrow(()->new RuntimeException("Lesson not owned or found"));

        Quiz quiz= Quiz.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .passingScore(dto.getPassingScore())
                .timeLimitInMinutes(dto.getTimeLimitInMinutes())
                .lesson(lesson)
                .build();

        quizRepo.save(quiz);
        return mapToQuizDto(quiz);
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


    public QuestionDto createQuestion(Long quizId, CreateQuestionDto dto) {

        User instructor = getCurrentInstructor();

        Quiz quiz = quizRepo
                .findByIdAndLessonCourseInstructor(quizId, instructor)
                .orElseThrow(() -> new RuntimeException("Quiz not found or not owned"));

        Question question = Question.builder()
                .question(dto.getQuestionText())
                .optionA(dto.getOptionA())
                .optionB(dto.getOptionB())
                .optionC(dto.getOptionC())
                .optionD(dto.getOptionD())
                .correctOption(dto.getCorrectOption())
                .quiz(quiz)
                .build();

        questionRepo.save(question);

        return mapToQuestionDto(question);
    }






    private CourseDto mapToCourseDto(Course course){
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

    private QuizDto mapToQuizDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setPassingScore(quiz.getPassingScore());
        dto.setTimeLimitInMins(quiz.getTimeLimitInMinutes());
        dto.setCreatedAt(quiz.getCreatedAt());
        dto.setTotalQuestions(quiz.getQuestions().size());


        Set<QuestionDto> questionDtos = quiz.getQuestions()
                .stream()
                .map(this::mapToQuestionDto)
                .collect(Collectors.toSet());

        dto.setQuestions(questionDtos);

        return dto;
    }

    private QuestionDto mapToQuestionDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestion());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setCorrectOption(question.getCorrectOption());

        // map other needed fields
        return dto;
    }

    private LessonDto mapToLessonDto(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .createdAt(lesson.getCreatedAt())
                .build();
    }

}
