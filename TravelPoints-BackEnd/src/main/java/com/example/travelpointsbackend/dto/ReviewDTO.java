package com.example.travelpointsbackend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {
    private Long reviewId;
    @NotNull(message = "UserID cannot be empty")
    private Long userId;
    @NotNull(message = "AttractionID cannot be empty")
    private Long attractionId;
    @NotNull(message = "ReviewText cannot be empty")
    private String reviewText;
    @NotNull(message = "Rating cannot be empty")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be no more than 5")
    private Integer rating;
    private Timestamp createdAt;
}
