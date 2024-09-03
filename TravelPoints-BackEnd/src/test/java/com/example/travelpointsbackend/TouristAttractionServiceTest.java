package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.model.AttractionDetail;
import com.example.travelpointsbackend.model.Category;
import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.service.implementation.TouristAttractionServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TouristAttractionServiceTest {
    @Mock
    TouristAttractionRepository touristAttractionRepository;

    @InjectMocks
    TouristAttractionServiceImplementation touristAttractionService;

    private static final String NAME = "Tourist Attraction";
    private static final String LOCATION = "Cluj";
    private static final String DESCRIPTION = "Test description";
    private static final String IMAGE_PATH = "test/image/path";
    private static final String OFFERS = "Test offers";
    private static final String TOURIST_ATTRACTION_REPOSITORY = "touristAttractionRepository";
    private static final Long ID = 1L;
    private static final String EXCEPTION_MESSAGE = "Test exception";
    private static final Float PRICE = 10.0f;

    private TouristAttractionDetailDTO createAttractionDetailDTO(){
        TouristAttractionDetailDTO touristAttractionDetailDTO = new TouristAttractionDetailDTO();
        touristAttractionDetailDTO.setName(NAME);
        touristAttractionDetailDTO.setLocation(LOCATION);
        touristAttractionDetailDTO.setCategory(Category.NATURE.name());
        touristAttractionDetailDTO.setDescriptionText(DESCRIPTION);
        touristAttractionDetailDTO.setImagePath(IMAGE_PATH);
        touristAttractionDetailDTO.setEntryPrice(PRICE);
        touristAttractionDetailDTO.setOffers(OFFERS);
        return touristAttractionDetailDTO;
    }

    private TouristAttraction createTouristAttraction(){
        TouristAttraction attraction = TouristAttraction.builder()
                .name(NAME)
                .location(LOCATION)
                .category(Category.NATURE)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        AttractionDetail detail = AttractionDetail.builder()
                .descriptionText(DESCRIPTION)
                .imagePath(IMAGE_PATH)
                .entryPrice(PRICE)
                .offers(OFFERS)
                .touristAttraction(attraction)
                .build();

        attraction.setAttractionDetail(detail);
        return attraction;
    }

    @BeforeEach()
    public void setup() {
        ReflectionTestUtils.setField(touristAttractionService, TOURIST_ATTRACTION_REPOSITORY, touristAttractionRepository);
    }

    @Test
    public void testDeleteTouristAttractionSuccess() {
        when(touristAttractionRepository.existsById(ID)).thenReturn(true);

        boolean result = touristAttractionService.deleteTouristAttraction(ID);

        verify(touristAttractionRepository).existsById(ID);

        assertTrue(result);
    }

    @Test
    public void testDeleteTouristAttractionFailure() {
        when(touristAttractionRepository.existsById(ID)).thenReturn(false);

        boolean result = touristAttractionService.deleteTouristAttraction(ID);

        verify(touristAttractionRepository).existsById(ID);

        assertFalse(result);
    }

    @Test
    public void testAddTouristAttractionSuccess() {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createAttractionDetailDTO();
        TouristAttraction attraction = createTouristAttraction();

        ReflectionTestUtils.setField(touristAttractionService, TOURIST_ATTRACTION_REPOSITORY, touristAttractionRepository);

        when(touristAttractionRepository.save(any(TouristAttraction.class))).thenReturn(attraction);

        Optional<TouristAttraction> result = touristAttractionService.addTouristAttraction(touristAttractionDetailDTO);

        verify(touristAttractionRepository).save(any(TouristAttraction.class));

        assertTrue(result.isPresent());
    }

    @Test
    public void testAddTouristAttractionFailure() {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createAttractionDetailDTO();

        when(touristAttractionRepository.save(any(TouristAttraction.class))).thenThrow(new DataAccessException(EXCEPTION_MESSAGE) {});

        Optional<TouristAttraction> result = touristAttractionService.addTouristAttraction(touristAttractionDetailDTO);

        verify(touristAttractionRepository).save(any(TouristAttraction.class));

        assertFalse(result.isPresent());
    }

    @Test
    public void  testUpdateTouristAttractionSuccess() {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createAttractionDetailDTO();
        TouristAttraction attraction = createTouristAttraction();

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(attraction));

        boolean result = touristAttractionService.updateTouristAttraction(ID, touristAttractionDetailDTO);

        verify(touristAttractionRepository).findById(ID);
        verify(touristAttractionRepository).save(any(TouristAttraction.class));
        assertTrue(result);
    }

    @Test
    public void  testUpdateTouristAttractionFailure() {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createAttractionDetailDTO();

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.empty());

        boolean result = touristAttractionService.updateTouristAttraction(ID, touristAttractionDetailDTO);

        verify(touristAttractionRepository).findById(ID);
        verify(touristAttractionRepository,never()).save(any());
        assertFalse(result);
    }

    @Test
    public void testFindAllAttractionsSuccess() {
        TouristAttraction touristAttraction1 = createTouristAttraction();
        TouristAttraction touristAttraction2 = createTouristAttraction();
        List<TouristAttraction> list = new ArrayList<>();
        list.add(touristAttraction1);
        list.add(touristAttraction2);

        when(touristAttractionRepository.findAll()).thenReturn(list);

        List<TouristAttractionDetailDTO> result = touristAttractionService.findAllAttractions();

        verify(touristAttractionRepository).findAll();
        assertEquals(2, result.size());

    }

    @Test
    public void testFindAllAttractionsFailure() {
        List<TouristAttraction> list = new ArrayList<>();

        when(touristAttractionRepository.findAll()).thenReturn(list);

        List<TouristAttractionDetailDTO> result = touristAttractionService.findAllAttractions();

        verify(touristAttractionRepository).findAll();
        assertEquals(0, result.size());

    }
}
