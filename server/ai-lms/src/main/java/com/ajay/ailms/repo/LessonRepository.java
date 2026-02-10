package com.ajay.ailms.repo;

import com.ajay.ailms.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    int countByCourseId(Long courseId);

}