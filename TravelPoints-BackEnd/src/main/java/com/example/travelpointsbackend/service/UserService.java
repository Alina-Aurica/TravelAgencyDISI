package com.example.travelpointsbackend.service;

import com.example.travelpointsbackend.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {
    Optional<User> findById(Long id);
}
