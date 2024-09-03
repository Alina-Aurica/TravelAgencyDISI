package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.UserWishlistDTO;
import com.example.travelpointsbackend.model.*;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.repository.UserWishlistRepository;
import com.example.travelpointsbackend.service.implementation.UserWishlistServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserWishlistServiceTest {

    @Mock
    TouristAttractionRepository touristAttractionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserWishlistRepository userWishlistRepository;

    @InjectMocks
    UserWishlistServiceImplementation userWishlistService;

    private final String TOURIST_ATTRACTION_REPOSITORY = "touristAttractionRepository";
    private final String USER_REPOSITORY = "userRepository";
    private final String USER_WISHLIST_REPOSITORY = "userWishlistRepository";
    private static final String NAME = "Tourist Attraction";
    private static final String LOCATION = "Cluj";
    private static final String DESCRIPTION = "Test description";
    private static final String IMAGE_PATH = "test/image/path";
    private static final String OFFERS = "Test offers";
    private static final Float PRICE = 10.0f;
    private static final String EMAIL = "clau@gmail.com";
    private static final String PASSWORD = "123456789";
    private static final Long ID = 1L;

    @BeforeEach()
    public void setup() {
        ReflectionTestUtils.setField(userWishlistService, TOURIST_ATTRACTION_REPOSITORY, touristAttractionRepository);
        ReflectionTestUtils.setField(userWishlistService, USER_REPOSITORY, userRepository);
        ReflectionTestUtils.setField(userWishlistService, USER_WISHLIST_REPOSITORY, userWishlistRepository);
    }

    private TouristAttraction createTouristAttraction(){
        TouristAttraction attraction = TouristAttraction.builder()
                .attractionId(ID)
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

    User createUser() {
        return  User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .userId(ID)
                .userType(UserType.TOURIST)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .isLogged(true)
                .build();
    }

    UserWishlist createWishList(User user){
        return  UserWishlist.builder()
                .addedAt(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .touristAttractionList(new ArrayList<>())
                .build();
    }

    @Test
    public void testAddTouristAttractionWithNoWishlistSuccess() {
        User user = createUser();
        TouristAttraction touristAttraction = createTouristAttraction();
        UserWishlist userWishlist = createWishList(user);
        user.setUserWishlist(userWishlist);

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(touristAttraction));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        boolean result = userWishlistService.addTouristAttraction(ID,ID);

        verify(touristAttractionRepository).findById(ID);
        verify(userRepository).findById(ID);
        verify(userWishlistRepository).save(any(UserWishlist.class));
        assertTrue(result);

    }

    @Test
    public void testAddTouristAttractionWithWishlistSuccess() {
        User user = createUser();

        TouristAttraction touristAttraction = createTouristAttraction();

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(touristAttraction));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        boolean result = userWishlistService.addTouristAttraction(ID,ID);

        verify(touristAttractionRepository).findById(ID);
        verify(userRepository).findById(ID);
        verify(userWishlistRepository).save(any(UserWishlist.class));
        assertTrue(result);

    }

    @Test
    public void testAddTouristAttractionFailure() {

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.empty());
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        boolean result = userWishlistService.addTouristAttraction(ID,ID);

        verify(touristAttractionRepository).findById(ID);
        verify(userRepository).findById(ID);
        verify(userWishlistRepository,times(0)).save(any(UserWishlist.class));
        assertFalse(result);

    }

    @Test
    public void testAddDuplicateTouristAttraction(){
        User user = createUser();
        TouristAttraction touristAttraction = createTouristAttraction();
        UserWishlist userWishlist = createWishList(user);
        user.setUserWishlist(userWishlist);
        touristAttraction.setUserWishlistList(new ArrayList<>());
        touristAttraction.getUserWishlistList().add(userWishlist);
        userWishlist.getTouristAttractionList().add(touristAttraction);

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(touristAttraction));
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        boolean result = userWishlistService.addTouristAttraction(ID,ID);

        verify(touristAttractionRepository).findById(ID);
        verify(userRepository).findById(ID);
        verify(userWishlistRepository,times(0)).save(any(UserWishlist.class));
        assertFalse(result);

    }

    @Test
    public void testGetUserWishlistSuccess(){
        User user = createUser();
        UserWishlist userWishlist = createWishList(user);
        TouristAttraction touristAttraction = createTouristAttraction();
        user.setUserWishlist(userWishlist);
        touristAttraction.setUserWishlistList(new ArrayList<>());
        touristAttraction.getUserWishlistList().add(userWishlist);
        userWishlist.getTouristAttractionList().add(touristAttraction);
        UserWishlistDTO userWishlistDTOCorrect = new UserWishlistDTO();
        userWishlistDTOCorrect.setUserId(ID);
        userWishlistDTOCorrect.setWishlistId(userWishlist.getWishlistId());
        userWishlistDTOCorrect.setAddedAt(userWishlist.getAddedAt());
        userWishlistDTOCorrect.setAttractionsIds(new ArrayList<>());
        userWishlistDTOCorrect.getAttractionsIds().add(touristAttraction.getAttractionId());
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        UserWishlistDTO userWishlistDTO = userWishlistService.getUserWishlist(ID);

        verify(userRepository).findById(ID);

        assertEquals(userWishlistDTO,userWishlistDTOCorrect);
    }

    @Test
    public void testGetUserWishlistNoUser(){
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        UserWishlistDTO userWishlistDTO = userWishlistService.getUserWishlist(ID);

        verify(userRepository).findById(ID);

        assertNull(userWishlistDTO);
    }

    @Test
    public void testGetUserWishlistNoWishlist(){
        User user = createUser();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        UserWishlistDTO userWishlistDTO = userWishlistService.getUserWishlist(ID);

        verify(userRepository).findById(ID);

        assertNull(userWishlistDTO);
    }

}
