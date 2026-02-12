package com.ajay.ailms.repo;

import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.entity.Course;
import com.ajay.ailms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
    SELECT c FROM Course c
    JOIN FETCH c.instructor
    """)
    Page<Course> findAllWithInstructor(Pageable pageable);

    @Query("""
    SELECT e.course
    FROM Enrollment e
    WHERE e.user.id = :studentId
    """)
    Page<Course> findCoursesByStudentId(Long studentId, Pageable pageable);

    Optional<Course> findByIdAndInstructor(Long courseId, User instructor);
}
