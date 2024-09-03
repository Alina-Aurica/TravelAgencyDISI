package com.example.travelpointsbackend.dto;

import com.example.travelpointsbackend.validation.ValidCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TouristAttractionDetailDTO {
    private Long attractionId;
    @NotNull(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Location cannot be empty")
    private String location;
    @NotNull(message = "Category cannot be empty")
    @ValidCategory
    private String category;
    @NotNull(message = "DescriptionText cannot be null")
    private String descriptionText;
    @NotNull(message = "ImagePath cannot be null")
    private String imagePath;
    @NotNull(message = "EntryPrice cannot be null")
    private Float entryPrice;
    @NotNull(message = "Offers cannot be null")
    private String offers;
    private Timestamp createdAt;

}
