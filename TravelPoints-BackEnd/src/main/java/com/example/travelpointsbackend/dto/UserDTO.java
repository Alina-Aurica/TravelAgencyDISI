package com.example.travelpointsbackend.dto;

import com.example.travelpointsbackend.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    @NotNull(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private UserType userType;
    private Timestamp createdAt;
    private boolean isLogged;
}
