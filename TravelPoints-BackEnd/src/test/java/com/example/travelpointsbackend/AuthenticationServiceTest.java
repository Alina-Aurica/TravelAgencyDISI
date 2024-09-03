package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.UserDTO;
import com.example.travelpointsbackend.dto.UserLogInDTO;
import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.model.UserType;
import com.example.travelpointsbackend.model.auth.AuthenticationResponse;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.service.implementation.AuthenticationServiceImplementation;
import com.example.travelpointsbackend.service.implementation.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.sql.Timestamp;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImplementation authenticationService;
    private static final String NAME = "Claudiu";

    private static final String EMAIL = "clau@gmail.com";
    private static final String PASSWORD = "123456789";
    private static final String TOKEN = "token";
    private static final String AUTHENTICATION_FAILED = "Authentication failed";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String EXCEPTION = "exception";
    private static final Long ID = 1L;

    UserDTO createUser() {
        return new UserDTO(ID, NAME, EMAIL, PASSWORD, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);
    }

    UserLogInDTO createUserForLogIn() {
        return new UserLogInDTO(EMAIL, PASSWORD);
    }

    UserDTO createAdministrator() {
        return new UserDTO(1L, NAME, EMAIL, PASSWORD,UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);
    }

    @Test
    void testLoginSuccess(){
        UserLogInDTO userDTO = createUserForLogIn();
        User user = new User();
        user.setEmail(EMAIL);
        user.setLogged(false);
        Authentication authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(userRepository.findByEmail(EMAIL)).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(TOKEN);

        AuthenticationResponse response = authenticationService.login(userDTO);

        assertEquals(TOKEN, response.getToken());
        verify(userRepository).save(user);
        assertTrue(user.isLogged());
    }

    @Test
    public void testLogin_Failure() {
       UserLogInDTO userDTO = createUserForLogIn();

        doThrow(new RuntimeException(AUTHENTICATION_FAILED)).when(authenticationManager)
                .authenticate(any());

        AuthenticationResponse response = authenticationService.login(userDTO);

        assertNull(response);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testRegisterTouristSuccess() {
        UserDTO userDTO = createUser();
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(ENCODED_PASSWORD);
        user.setUserType(UserType.TOURIST);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(TOKEN);

        AuthenticationResponse response = authenticationService.registerTourist(userDTO);

        assertEquals(TOKEN, response.getToken());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    public void testRegisterTouristFailure() {
        UserDTO userDTO = createUser();
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        AuthenticationResponse response = authenticationService.registerTourist(userDTO);

        assertEquals(null, response);
    }

    @Test
    public void testRegisterAdministratorSuccess() {
        UserDTO userDTO = createUser();
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(ENCODED_PASSWORD);
        user.setUserType(UserType.ADMINISTRATOR);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(TOKEN);

        AuthenticationResponse response = authenticationService.registerTourist(userDTO);

        assertEquals(TOKEN, response.getToken());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    public void testRegisterAdministratorFailure() {
        UserDTO userDTO = createUser();
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        AuthenticationResponse response = authenticationService.registerTourist(userDTO);

        assertEquals(null, response);
    }

    @Test
    public void testLogoutSuccess() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setLogged(true);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = authenticationService.logOut(ID);

        assertTrue(result);
        assertFalse(user.isLogged());
        verify(userRepository).save(user);
    }

    @Test
    public void testLogoutUserNotLoggedIn() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setLogged(false);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        boolean result = authenticationService.logOut(ID);

        assertFalse(result);
        assertFalse(user.isLogged());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void testLogoutUserNotFound() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        boolean result = authenticationService.logOut(ID);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}
