package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
