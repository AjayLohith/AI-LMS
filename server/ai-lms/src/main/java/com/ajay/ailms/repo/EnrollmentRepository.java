package com.ajay.ailms.repo;

import com.ajay.ailms.entity.Course;
import com.ajay.ailms.entity.Enrollment;
import com.ajay.ailms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
}