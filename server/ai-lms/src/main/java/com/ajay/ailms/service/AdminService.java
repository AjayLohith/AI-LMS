package com.ajay.ailms.service;

import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.dto.UserDto;
import com.ajay.ailms.entity.Course;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.entity.type.Role;
import com.ajay.ailms.repo.CourseRepository;
import com.ajay.ailms.repo.EnrollmentRepository;
import com.ajay.ailms.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final AIService aiService;

    public User getCurrentUser(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo
                .findByEmail(email).orElseThrow(()->new RuntimeException("User does not exist"));
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(this::mapToUserDto);
    }

    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepo.findAll(pageable)
                .map(this::mapToCourseDto);
    }



    public String promoteUser(Long userId) {
        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("User does not exists"));

        if(user.getRole()==Role.INSTRUCTOR){
            throw new RuntimeException("User is already Instructor");
        }

        user.setRole(Role.INSTRUCTOR);
        userRepo.save(user);

        return "Promoted Student Successfuly";
    }

    public String demoteUser(Long userId) {
        User user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("User does not exists"));

        if(user.getRole()==Role.STUDENT){
            throw new RuntimeException("User is already Student");
        }

        user.setRole(Role.STUDENT);
        userRepo.save(user);

        return "Demoted Student Successfuly";
    }

    public String deleteUser(Long id) {
        User user=userRepo.findById(id)
                .orElseThrow(()->new RuntimeException("User does not exists"));
        userRepo.delete(user);
        return "User deleted succesfully";
    }






    private UserDto mapToUserDto(User user) {
        UserDto dto=new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setRole(String.valueOf(user.getRole()));
        dto.setEmail(user.getEmail());
        return dto;
    }

    private CourseDto mapToCourseDto(Course course) {
        CourseDto dto=new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructor().getUsername());
        dto.setDifficulty(course.getDifficulty());
        dto.setRating(course.getRating());
        dto.setCreatedAt(course.getCreatedAt());
        return dto;
    }



}
