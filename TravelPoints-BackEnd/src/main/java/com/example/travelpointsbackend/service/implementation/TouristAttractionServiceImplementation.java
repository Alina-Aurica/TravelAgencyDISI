package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.model.AttractionDetail;
import com.example.travelpointsbackend.model.Category;
import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.service.TouristAttractionService;
import com.example.travelpointsbackend.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TouristAttractionServiceImplementation implements TouristAttractionService {

    private final TouristAttractionRepository touristAttractionRepository;
    @Override
    @Transactional
    public Optional<TouristAttraction> addTouristAttraction(TouristAttractionDetailDTO touristAttractionDetailDto) {
        TouristAttraction attraction = TouristAttraction.builder()
                .name(touristAttractionDetailDto.getName())
                .location(touristAttractionDetailDto.getLocation())
                .category(Category.valueOf(touristAttractionDetailDto.getCategory()))
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        AttractionDetail detail = AttractionDetail.builder()
                .descriptionText(touristAttractionDetailDto.getDescriptionText())
                .imagePath(touristAttractionDetailDto.getImagePath())
                .entryPrice(touristAttractionDetailDto.getEntryPrice())
                .offers(touristAttractionDetailDto.getOffers())
                .touristAttraction(attraction)
                .build();

        attraction.setAttractionDetail(detail);

        try {
            return Optional.of(touristAttractionRepository.save(attraction));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    @Transactional
    public boolean deleteTouristAttraction(Long id) {
        if (touristAttractionRepository.existsById(id)) {
            touristAttractionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateTouristAttraction(Long id, TouristAttractionDetailDTO dto) {
        Optional<TouristAttraction> existingAttraction = touristAttractionRepository.findById(id);
        if (existingAttraction.isPresent()) {
            TouristAttraction attraction = existingAttraction.get();
            attraction.setName(dto.getName());
            attraction.setLocation(dto.getLocation());
            attraction.setCategory(Category.valueOf(dto.getCategory()));

            AttractionDetail detail = attraction.getAttractionDetail();
            detail.setDescriptionText(dto.getDescriptionText());
            detail.setImagePath(dto.getImagePath());
            detail.setEntryPrice(dto.getEntryPrice());
            detail.setOffers(dto.getOffers());

            touristAttractionRepository.save(attraction);
            return true;
        } else {
            return false;
        }
    }

    public List<TouristAttractionDetailDTO> findAllAttractions() {
        List<TouristAttraction> attractions = touristAttractionRepository.findAll();
        return attractions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TouristAttractionDetailDTO convertToDTO(TouristAttraction attraction) {
        AttractionDetail detail = attraction.getAttractionDetail();
        return new TouristAttractionDetailDTO(
                attraction.getAttractionId(),
                attraction.getName(),
                attraction.getLocation(),
                attraction.getCategory().name(),
                detail.getDescriptionText(),
                detail.getImagePath(),
                detail.getEntryPrice(),
                detail.getOffers(),
                attraction.getCreatedAt()
        );
    }

}
