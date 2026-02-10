package com.ajay.ailms.repo;

import com.ajay.ailms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);
}