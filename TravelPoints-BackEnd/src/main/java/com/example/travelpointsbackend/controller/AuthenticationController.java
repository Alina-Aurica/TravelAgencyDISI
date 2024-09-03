package com.example.travelpointsbackend.controller;

import com.example.travelpointsbackend.dto.UserDTO;
import com.example.travelpointsbackend.dto.UserLogInDTO;
import com.example.travelpointsbackend.model.auth.AuthenticationResponse;
import com.example.travelpointsbackend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private static final String CHECK_LOGOUT_FAIL = "{\"message\": \"LogOut - not work!\"}" ;
    private static final String CHECK_LOGOUT_SUCCESS = "{\"message\": \"LogOut - work!\"}";
    private static final String DUPLICATE_EMAIL = "{\"message\": \"Duplicate email!\"}";
    private static final String BAD_CREDENTIALS = "{\"message\": \"Bad credentials!\"}";
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity registerTourist(@Valid @RequestBody UserDTO userDTO){
        AuthenticationResponse response = authenticationService.registerTourist(userDTO);
        if(Objects.isNull(response)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DUPLICATE_EMAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/registerAdministrator")
    public ResponseEntity registerAdministrator(@Valid @RequestBody UserDTO userDTO){
        AuthenticationResponse response = authenticationService.registerAdministrator(userDTO);
        if(Objects.isNull(response)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DUPLICATE_EMAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserLogInDTO userDTO ){
        AuthenticationResponse response = authenticationService.login(userDTO);
        if(Objects.isNull(response)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_CREDENTIALS);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable Long userId){
        boolean checkLogout = authenticationService.logOut(userId);
        if (!checkLogout){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CHECK_LOGOUT_FAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(CHECK_LOGOUT_SUCCESS);
    }
}
