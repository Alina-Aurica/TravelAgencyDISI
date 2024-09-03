package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.dto.UserDTO;
import com.example.travelpointsbackend.dto.UserLogInDTO;
import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.model.UserType;
import com.example.travelpointsbackend.model.auth.AuthenticationResponse;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse registerTourist(UserDTO userDTO) {
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUserType(UserType.TOURIST);
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch(Exception exception) {
            log.error("{Error}", exception);
        }

        return null;
    }

    @Override
    public AuthenticationResponse registerAdministrator(UserDTO userDTO) {
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setUserType(UserType.ADMINISTRATOR);
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch(Exception exception) {
            log.error("{Error}", exception);
        }

        return null;
    }

    @Override
    public AuthenticationResponse login(UserLogInDTO userDTO) {
        try {
            String email = userDTO.getEmail();
            String password = userDTO.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            var user = userRepository.findByEmail(email)
                    .orElseThrow();
            user.setLogged(true);
            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception exception) {
            log.error("{Error}", exception);
        }

        return null;
    }

    @Override
    public boolean logOut(Long userId) {
        try {
            var user = userRepository.findById(userId).orElseThrow();
            if(user.isLogged()){
                user.setLogged(false);
                userRepository.save(user);
                // Verified functionality of save() method
                return !user.isLogged();
            }
        } catch (Exception exception) {
            log.error("{Error}", exception);
        }

        // Returns false due to an exception thrown by findById
        return false;
    }
}
