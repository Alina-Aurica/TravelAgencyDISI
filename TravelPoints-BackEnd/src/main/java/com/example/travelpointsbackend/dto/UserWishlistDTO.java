package com.example.travelpointsbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserWishlistDTO {
    private Long wishlistId;
    @NotNull(message = "UserID cannot be empty")
    private Long userId;
    @NotNull(message = "AttractionsID cannot be empty")
    private List<Long> attractionsIds;
    private Timestamp addedAt;

}
