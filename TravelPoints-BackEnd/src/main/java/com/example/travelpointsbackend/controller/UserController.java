package com.example.travelpointsbackend.controller;

import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }
}
