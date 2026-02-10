package com.ajay.ailms.repo;

import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
}
