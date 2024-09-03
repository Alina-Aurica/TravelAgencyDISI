package com.example.travelpointsbackend.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitStatisticDTO {
    private Long statisticId;
    @NotNull(message = "AttractionID cannot be empty")
    private Long attractionId;
    private Timestamp visitDate;

}
