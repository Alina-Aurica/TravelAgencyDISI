package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.ReviewDTO;
import com.example.travelpointsbackend.dto.ReviewWithUserNameDTO;
import com.example.travelpointsbackend.model.*;
import com.example.travelpointsbackend.repository.ReviewRepository;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.service.implementation.ReviewServiceImplementation;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TouristAttractionRepository touristAttractionRepository;
    @InjectMocks
    ReviewServiceImplementation reviewServiceImplementation;
    private static final String REVIEW_REPOSITORY = "reviewRepository";
    private static final String USER_REPOSITORY = "userRepository";
    private static final String ATTRACTION_REPOSITORY = "touristAttractionRepository";
    // for review
    private static final Long REVIEW_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long ATTRACTION_ID = 1L;
    private static final String REVIEW_TEXT = "good";
    private static final Integer RATING = 3;

    // for attraction
    private static final Long DETAIL_ID = 1L;
    private static final String NAME = "Tourist Attraction";
    private static final String LOCATION = "Cluj";
    private static final String DESCRIPTION = "Test description";
    private static final String IMAGE_PATH = "test/image/path";
    private static final String OFFERS = "Test offers";
    private static final Float PRICE = 10.0f;

    // for user
    private static final String EMAIL = "ana@gmail.com";
    private static final String PASSWORD = "anaaremere";


    private ReviewDTO createReviewDTO() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(USER_ID);
        reviewDTO.setAttractionId(ATTRACTION_ID);
        reviewDTO.setReviewText(REVIEW_TEXT);
        reviewDTO.setRating(RATING);
        return reviewDTO;
    }

    private User createUser() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setUserType(UserType.TOURIST);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        userRepository.save(user);
        return user;
    }

    private TouristAttraction createTouristAttraction() {
        TouristAttraction attraction = new TouristAttraction();
        attraction.setAttractionId(ATTRACTION_ID);
        attraction.setName(NAME);
        attraction.setLocation(LOCATION);
        attraction.setCategory(Category.NATURE);
        attraction.setCreatedAt(Timestamp.from(Instant.now()));

        AttractionDetail detail = new AttractionDetail();
        detail.setDetailId(DETAIL_ID);
        detail.setDescriptionText(DESCRIPTION);
        detail.setImagePath(IMAGE_PATH);
        detail.setEntryPrice(PRICE);
        detail.setOffers(OFFERS);
        detail.setTouristAttraction(attraction);

        attraction.setAttractionDetail(detail);
        touristAttractionRepository.save(attraction);

        return attraction;
    }

    private Review createReview() {
        Review review = new Review();
        User user = createUser();
        TouristAttraction touristAttraction = createTouristAttraction();
        review.setUser(user);
        review.setTouristAttraction(touristAttraction);
        review.setReviewText(REVIEW_TEXT);
        review.setRating(RATING);
        review.setCreatedAt(Timestamp.from(Instant.now()));
        return review;
    }

    @BeforeEach()
    public void setup() {
        ReflectionTestUtils.setField(reviewServiceImplementation, REVIEW_REPOSITORY, reviewRepository);
        ReflectionTestUtils.setField(reviewServiceImplementation, USER_REPOSITORY, userRepository);
        ReflectionTestUtils.setField(reviewServiceImplementation, ATTRACTION_REPOSITORY, touristAttractionRepository);
    }

    @Test
    public void testAddReviewSuccess() {
        ReviewDTO reviewDTO = createReviewDTO();
        Review review = createReview();
        User user = createUser();
        TouristAttraction touristAttraction = createTouristAttraction();

        ReflectionTestUtils.setField(reviewServiceImplementation, REVIEW_REPOSITORY, reviewRepository);
        ReflectionTestUtils.setField(reviewServiceImplementation, USER_REPOSITORY, userRepository);
        ReflectionTestUtils.setField(reviewServiceImplementation, ATTRACTION_REPOSITORY, touristAttractionRepository);

        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(touristAttractionRepository.findById(ATTRACTION_ID)).thenReturn(Optional.of(touristAttraction));

        Optional<Review> result = reviewServiceImplementation.addReview(reviewDTO);

        verify(reviewRepository).save(any(Review.class));

        assertTrue(result.isPresent());
    }

    @Test
    public void testAddReviewFailure() {
        ReviewDTO reviewDTO = createReviewDTO();
        User user = createUser();
        TouristAttraction touristAttraction = createTouristAttraction();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        when(touristAttractionRepository.findById(ATTRACTION_ID)).thenReturn(Optional.of(touristAttraction));

        Optional<Review> result = reviewServiceImplementation.addReview(reviewDTO);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateReviewSuccess() {
        ReviewDTO reviewDTO = createReviewDTO();
        Review review = createReview();

        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));

        boolean result = reviewServiceImplementation.updateReview(REVIEW_ID, reviewDTO);

        verify(reviewRepository).findById(REVIEW_ID);
        verify(reviewRepository).save(any(Review.class));
        assertTrue(result);
    }

    @Test
    public void testUpdateReviewFailed() {
        ReviewDTO reviewDTO = createReviewDTO();

        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.empty());

        boolean result = reviewServiceImplementation.updateReview(REVIEW_ID, reviewDTO);

        verify(reviewRepository).findById(REVIEW_ID);
        verify(reviewRepository, never()).save(any());
        assertFalse(result);
    }

    @Test
    public void testDeleteReviewSuccess() {
        when(reviewRepository.existsById(REVIEW_ID)).thenReturn(true);

        boolean result = reviewServiceImplementation.deleteReview(REVIEW_ID);

        verify(reviewRepository).existsById(REVIEW_ID);

        assertTrue(result);
    }

    @Test
    public void testDeleteReviewFailure() {
        when(reviewRepository.existsById(REVIEW_ID)).thenReturn(false);

        boolean result = reviewServiceImplementation.deleteReview(REVIEW_ID);

        verify(reviewRepository).existsById(REVIEW_ID);

        assertFalse(result);
    }

    @Test
    public void testFindAllReviewsSuccess() {
        Review review1 = createReview();
        Review review2 = createReview();
        List<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);
        TouristAttraction touristAttraction = createTouristAttraction();

        when(reviewRepository.findAllByTouristAttraction(touristAttraction)).thenReturn(list);
        when(touristAttractionRepository.findById(ATTRACTION_ID)).thenReturn(Optional.of(touristAttraction));

        List<ReviewWithUserNameDTO> result = reviewServiceImplementation.findAllReviewByTouristAttractionId(ATTRACTION_ID);

        verify(reviewRepository).findAllByTouristAttraction(touristAttraction);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllReviewsFailure() {
        List<Review> list = new ArrayList<>();

        TouristAttraction touristAttraction = createTouristAttraction();

        when(reviewRepository.findAllByTouristAttraction(touristAttraction)).thenReturn(list);
        when(touristAttractionRepository.findById(ATTRACTION_ID)).thenReturn(Optional.of(touristAttraction));

        List<ReviewWithUserNameDTO> result = reviewServiceImplementation.findAllReviewByTouristAttractionId(ATTRACTION_ID);

        verify(reviewRepository).findAllByTouristAttraction(touristAttraction);
        assertEquals(0, result.size());
    }
}
