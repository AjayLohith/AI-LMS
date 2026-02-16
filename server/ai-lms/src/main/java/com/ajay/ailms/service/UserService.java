package com.ajay.ailms.service;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.*;
import com.ajay.ailms.repo.CourseRepository;
import com.ajay.ailms.repo.EnrollmentRepository;
import com.ajay.ailms.repo.QuizRepository;
import com.ajay.ailms.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public StudentQuizDto attemptQuiz(Long quizId) {
        Quiz quiz=quizRepo.findById(quizId).orElseThrow(()->new RuntimeException("Quiz doesnt exist"));

        User student=getCurrentUser();
        Course course=quiz.getLesson().getCourse();

        boolean enrolled=enrollmentRepo.existsByUserAndCourse(student,course);
        if(!enrolled){
            throw new RuntimeException("Student not enrolled to this course");
        }
        List<QuestionViewDto>questionViewDtos=quiz.getQuestions()
                .stream()
                .map(question -> QuestionViewDto.builder()
                        .id(question.getId())
                        .questionText(question.getQuestion())
                        .options(List.of(
                                question.getOptionA(),
                                question.getOptionB(),
                                question.getOptionC(),
                                question.getOptionD()
                        ))
                        .build()
                )
                .toList();

        return StudentQuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .timeLimitInMins(quiz.getTimeLimitInMinutes())
                .questions(questionViewDtos)
                .build();
    }

    public QuizResultDto submitQuiz(SubmitQuizDto dto) {
        User student=getCurrentUser();

        Quiz quiz=quizRepo.findById(dto.getQuizId())
                .orElseThrow(()->new RuntimeException("Quiz not found"));

        Course course=quiz.getLesson().getCourse();
        boolean enrolled=enrollmentRepo.existsByUserAndCourse(student,course);

        if(!enrolled){
            throw new RuntimeException("Student not enrolled in this course");
        }

        Map<Long, Question>questionMap=quiz.getQuestions()
                .stream()
                .collect(Collectors.toMap(Question::getId,q->q));

        int correctCount=0;
        for(AnswerDto answer:dto.getAnswers()){
            Question question=questionMap.get(answer.getQuestionId());

            if(question==null){
                throw new RuntimeException("Invalid question for this quiz");
            }
            if(!List.of("A","B","C","D")
                    .contains(answer.getSelectedOption())){
                throw new RuntimeException("Invalid option selected");
            }

            if(question.getCorrectOption()
                    .equalsIgnoreCase(answer.getSelectedOption())){
                correctCount++;
            }
        }
        int totalQuestions = questionMap.size();
        double percentage=(correctCount * 100.0)/totalQuestions;
        boolean passed=percentage >= quiz.getPassingScore();

        if(passed){
            Enrollment enrollment=enrollmentRepo
                    .findByUserAndCourse(student,course)
                    .orElseThrow();

            enrollment.setCompletedLessons(
                    enrollment.getCompletedLessons()+1
            );

            enrollmentRepo.save(enrollment);
        }
        return QuizResultDto.builder()
                .quizId(quiz.getId())
                .totalQuestions(totalQuestions)
                .correctOptions(correctCount)
                .scorePercentage(percentage)
                .passed(passed)
                .build();
    }

    @Transactional
    public String enrollCourse(Long courseId) {
        User user=getCurrentUser();

        Course course=courseRepo.findById(courseId).orElseThrow(()->new RuntimeException("Course does not exist"));

        boolean enrolled=enrollmentRepo.existsByUserAndCourse(user,course);

        if(enrolled){
            throw new RuntimeException("User already enrolled in this course");
        }

        Enrollment enrollment=Enrollment.builder()
                .user(user)
                .course(course)
                .status(true)
                .completedLessons(0)
                .completionPercentage(0.0)
                .build();

        enrollmentRepo.save(enrollment);
        return "Enrollment successful";
    }
}
