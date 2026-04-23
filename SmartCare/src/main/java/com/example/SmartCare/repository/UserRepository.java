package com.example.SmartCare.repository;


import com.example.SmartCare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
   Optional<User> findByEmail(String email);
}
