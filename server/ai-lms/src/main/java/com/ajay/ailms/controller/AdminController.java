package com.ajay.ailms.controller;

import com.ajay.ailms.dto.AdminDto;
import com.ajay.ailms.dto.CourseDto;
import com.ajay.ailms.dto.UserDto;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;


//    GET /admin/courses---done

//    DELETE /admin/courses/{id}

//    PUT /admin/courses/{id}/disable

    public ResponseEntity<Page<CourseDto>>getAllCourses(
            @PageableDefault(
                    size = 10,
                    sort="createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ){
        return ResponseEntity.ok(adminService.getAllCourses(pageable));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>>getAllUsers(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ){
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @PutMapping("/users/{id}/promote")
    public ResponseEntity<String>promote(@PathVariable Long id){
        return ResponseEntity.ok(adminService.promoteUser(id));
    }

    @PutMapping ("/users/{id}/demote")
    public ResponseEntity<String>demote(@PathVariable Long id){
        return ResponseEntity.ok(adminService.demoteUser(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String>deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(adminService.deleteUser(id));
    }


}
