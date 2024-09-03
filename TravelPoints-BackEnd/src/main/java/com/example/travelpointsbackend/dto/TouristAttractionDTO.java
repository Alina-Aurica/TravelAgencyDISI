package com.example.travelpointsbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TouristAttractionDTO {
    private Long attractionId;
    @NotNull(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Location cannot be empty")
    private String location;
    @NotNull(message = "Category cannot be empty")
    private String category;
    private Timestamp createdAt;
}
