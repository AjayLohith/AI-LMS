package com.ajay.ailms.service;

import com.ajay.ailms.dto.*;
import com.ajay.ailms.entity.*;
import com.ajay.ailms.repo.CourseRepository;
import com.ajay.ailms.repo.EnrollmentRepository;
import com.ajay.ailms.repo.QuizRepository;
import com.ajay.ailms.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public StudentQuizDto attemptQuiz(Long quizId, QuestionViewDto dto) {

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


}
