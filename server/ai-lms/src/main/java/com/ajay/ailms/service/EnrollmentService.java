package com.ajay.ailms.service;

import com.ajay.ailms.entity.Course;
import com.ajay.ailms.entity.Enrollment;
import com.ajay.ailms.entity.Lesson;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.repo.CourseRepository;
import com.ajay.ailms.repo.EnrollmentRepository;
import com.ajay.ailms.repo.LessonRepository;
import com.ajay.ailms.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final LessonRepository lessonRepo;

    private User getCurrentUser(){
        String email=SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
    }
    public String enroll(Long courseId) {
        Course course=courseRepo.findById(courseId)
                .orElseThrow(()->new RuntimeException("Course not found"));

        User user=this.getCurrentUser();
        boolean exists=enrollmentRepo.existsByUserAndCourse(user,course);

        if(exists){
            throw new RuntimeException("User enrolled already for this course");
        }

        Enrollment enrollment=Enrollment.builder()
                .user(user)
                .course(course)
                .completedLessons(0)
                .completionPercentage(0.0)
                .status(true)
                .build();

        enrollmentRepo.save(enrollment);
        return "Enrollment Successfull";
    }

    public void completeLesson(Long courseId, Long lessonId) {
        Long studentId= getCurrentUser().getId();
        Enrollment enrollment=enrollmentRepo.findByUserIdAndCourseId(studentId,courseId)
                .orElseThrow(()->new RuntimeException("User not enrolled"));

        Lesson lesson=lessonRepo.findById(lessonId)
                .orElseThrow(()->new RuntimeException("Lesson not found"));

        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new RuntimeException("Lesson does not belong to this course");
        }

        int totalLessons = lessonRepo.countByCourseId(courseId);

        if (enrollment.getCompletedLessons() >= totalLessons) {
            return; // already completed all lessons
        }
        enrollment.setCompletedLessons(enrollment.getCompletedLessons()+1);

        enrollment.setCompletionPercentage(
                enrollment.getCompletedLessons() * 100.0 / totalLessons
        );
    }
}
