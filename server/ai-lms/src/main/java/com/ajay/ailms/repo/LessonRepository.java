package com.ajay.ailms.repo;

import com.ajay.ailms.entity.Lesson;
import com.ajay.ailms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    int countByCourseId(Long courseId);

    Optional<Lesson> findByIdAndCourseInstructor(Long lessonId, User instructor);
}