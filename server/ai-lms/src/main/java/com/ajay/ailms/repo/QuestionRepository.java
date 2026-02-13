package com.ajay.ailms.repo;

import com.ajay.ailms.entity.Question;
import com.ajay.ailms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndQuizLessonCourseInstructor(Long questionId, User instructor);
}