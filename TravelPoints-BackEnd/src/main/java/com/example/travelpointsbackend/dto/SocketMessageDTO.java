package com.example.travelpointsbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketMessageDTO {
    @NotNull(message = "User cannot be null")
    private Long userId;
    @NotNull(message = "Name product cannot pe null")
    private String nameAttraction;
    @NotNull(message = "Offers cannot be null")
    private String offers;
}
