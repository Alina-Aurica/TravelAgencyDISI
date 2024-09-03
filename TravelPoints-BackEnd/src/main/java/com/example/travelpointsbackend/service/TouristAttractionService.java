package com.example.travelpointsbackend.service;

import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TouristAttractionService {

    Optional<TouristAttraction> addTouristAttraction(TouristAttractionDetailDTO touristAttractionDetailDTO);

    boolean deleteTouristAttraction(Long id);

    boolean updateTouristAttraction(Long id, TouristAttractionDetailDTO touristAttractionDetailDTO);
    public List<TouristAttractionDetailDTO> findAllAttractions();

}
