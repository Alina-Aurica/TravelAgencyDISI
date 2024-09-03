package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.SocketMessageDTO;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.model.*;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserWishlistRepository;
import com.example.travelpointsbackend.service.WebSocketService;
import com.example.travelpointsbackend.service.implementation.WebSocketServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WebSocketServiceTest {
    @Mock
    TouristAttractionRepository touristAttractionRepository;
    @Mock
    UserWishlistRepository userWishlistRepository;
    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    WebSocketServiceImplementation webSocketService;

    private static final String OBJECT_NOT_NULL = "Object is not null";
    private static final String OBJECT_NULL = "Object is null";
    // attraction
    private static final String TOURIST_ATTRACTION_REPOSITORY = "touristAttractionRepository";
    private static final String WISHLIST_REPOSITORY = "userWishlistRepository";
    private static final String NAME = "Tourist Attraction";
    private static final String LOCATION = "Cluj";
    private static final String DESCRIPTION = "Test description";
    private static final String IMAGE_PATH = "test/image/path";
    private static final String OFFERS = "Test offers";
    private static final String OFFERS_NEW = "Test offers 2";
    private static final Long ID = 1L;
    private static final Float PRICE = 10.0f;

    // wishlist
    private static final String USER_NAME = "Claudiu";
    private static final String EMAIL = "clau@gmail.com";
    private static final String PASSWORD = "123456789";

    private TouristAttractionDetailDTO createAttractionDetailDTO(){
        TouristAttractionDetailDTO touristAttractionDetailDTO = new TouristAttractionDetailDTO();
        touristAttractionDetailDTO.setName(NAME);
        touristAttractionDetailDTO.setLocation(LOCATION);
        touristAttractionDetailDTO.setCategory(Category.NATURE.name());
        touristAttractionDetailDTO.setDescriptionText(DESCRIPTION);
        touristAttractionDetailDTO.setImagePath(IMAGE_PATH);
        touristAttractionDetailDTO.setEntryPrice(PRICE);
        touristAttractionDetailDTO.setOffers(OFFERS_NEW);
        return touristAttractionDetailDTO;
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
                .name(USER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .userId(ID)
                .userType(UserType.TOURIST)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .isLogged(true)
                .build();
    }

    User createUser2() {
        return  User.builder()
                .name(USER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .userId(ID)
                .userType(UserType.TOURIST)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .isLogged(false)
                .build();
    }

    UserWishlist createWishList(User user){
        return  UserWishlist.builder()
                .addedAt(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .touristAttractionList(new ArrayList<>())
                .build();
    }

    @BeforeEach()
    public void setup() {
        ReflectionTestUtils.setField(webSocketService, TOURIST_ATTRACTION_REPOSITORY, touristAttractionRepository);
        ReflectionTestUtils.setField(webSocketService, WISHLIST_REPOSITORY, userWishlistRepository);
    }

    @Test
    public void testVerifyIfUpdateOffersAndUserLoggedSuccess(){
        TouristAttractionDetailDTO touristAttractionDTO = createAttractionDetailDTO();
        TouristAttraction touristAttraction = createTouristAttraction();
        User user = createUser();
        UserWishlist userWishlist = createWishList(user);
        user.setUserWishlist(userWishlist);
        touristAttraction.setUserWishlistList(new ArrayList<>());
        touristAttraction.getUserWishlistList().add(userWishlist);
        userWishlist.getTouristAttractionList().add(touristAttraction);
        List<UserWishlist> userWishlistLists = new ArrayList<UserWishlist>();
        userWishlistLists.add(userWishlist);

        SocketMessageDTO socketMessageDTO = new SocketMessageDTO(ID, touristAttractionDTO.getName(), touristAttractionDTO.getOffers());

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(touristAttraction));
        when(userWishlistRepository.findAll()).thenReturn(userWishlistLists);

        SocketMessageDTO response = webSocketService.verifyIfUpdateOffersAndUserLogged(ID, touristAttractionDTO);

        assertNotNull(OBJECT_NOT_NULL, response.toString());
    }

    @Test
    public void testVerifyIfUpdateOffersAndUserLoggedFailed(){
        TouristAttractionDetailDTO touristAttractionDTO = createAttractionDetailDTO();
        TouristAttraction touristAttraction = createTouristAttraction();
        User user = createUser2();
        UserWishlist userWishlist = createWishList(user);
        user.setUserWishlist(userWishlist);
        touristAttraction.setUserWishlistList(new ArrayList<>());
        touristAttraction.getUserWishlistList().add(userWishlist);
        userWishlist.getTouristAttractionList().add(touristAttraction);
        List<UserWishlist> userWishlistLists = new ArrayList<UserWishlist>();
        userWishlistLists.add(userWishlist);

        SocketMessageDTO socketMessageDTO = new SocketMessageDTO(ID, touristAttractionDTO.getName(), touristAttractionDTO.getOffers());

        when(touristAttractionRepository.findById(ID)).thenReturn(Optional.of(touristAttraction));
        when(userWishlistRepository.findAll()).thenReturn(userWishlistLists);

        SocketMessageDTO response = webSocketService.verifyIfUpdateOffersAndUserLogged(ID, touristAttractionDTO);

        assertNull(OBJECT_NULL, response);
    }

    @Test
    public void testSendMessageSuccess(){
        TouristAttractionDetailDTO touristAttractionDTO = createAttractionDetailDTO();
        SocketMessageDTO socketMessageDTO = new SocketMessageDTO(ID, touristAttractionDTO.getName(), touristAttractionDTO.getOffers());

        webSocketService.sendMessage(socketMessageDTO);

        verify(simpMessagingTemplate).convertAndSend("/topic/" + socketMessageDTO.getUserId(), socketMessageDTO);
    }
}
