package com.example.travelpointsbackend.service;

import com.example.travelpointsbackend.dto.UserDTO;
import com.example.travelpointsbackend.dto.UserLogInDTO;
import com.example.travelpointsbackend.model.auth.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationService {
    AuthenticationResponse registerTourist(UserDTO userDTO);
    AuthenticationResponse registerAdministrator(UserDTO userDTO);
    AuthenticationResponse login(UserLogInDTO userDTO);
    boolean logOut(Long userId);
}
