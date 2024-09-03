package com.example.travelpointsbackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttractionDetailDTO{
    private Long detailId;
    @NotNull(message = "AttractionID cannot be null")
    private Long attractionId;
    @NotNull(message = "DescriptionText cannot be null")
    private String descriptionText;
    @NotNull(message = "ImagePath cannot be null")
    private String imagePath;
    @NotNull(message = "EntryPrice cannot be null")
    private Float entryPrice;
    @NotNull(message = "Offers cannot be null")
    private String offers;

}
