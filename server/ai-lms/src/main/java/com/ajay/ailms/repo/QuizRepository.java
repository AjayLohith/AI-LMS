package com.ajay.ailms.repo;

import com.ajay.ailms.entity.Quiz;
import com.ajay.ailms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByIdAndLessonCourseInstructor(Long quizId, User instructor);

}