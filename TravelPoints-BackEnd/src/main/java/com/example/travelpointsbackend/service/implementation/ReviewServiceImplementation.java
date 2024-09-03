package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.dto.ReviewDTO;
import com.example.travelpointsbackend.dto.ReviewWithUserNameDTO;
import com.example.travelpointsbackend.model.Review;
import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.repository.ReviewRepository;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TouristAttractionRepository touristAttractionRepository;

    @Override
    public Optional<Review> addReview(ReviewDTO reviewDTO) {
        Optional<User> user = userRepository.findById(reviewDTO.getUserId());
        Optional<TouristAttraction> touristAttraction = touristAttractionRepository.findById(reviewDTO.getAttractionId());
        if (user.isPresent() && touristAttraction.isPresent()) {
            Review review = new Review();
            review.setUser(user.get());
            review.setTouristAttraction(touristAttraction.get());
            review.setReviewText(reviewDTO.getReviewText());
            review.setRating(reviewDTO.getRating());
            return Optional.of(reviewRepository.save(review));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateReview(Long id, ReviewDTO reviewDTO) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setReviewText(reviewDTO.getReviewText());
            review.setRating(reviewDTO.getRating());
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public List<ReviewWithUserNameDTO> findAllReviewByTouristAttractionId(Long attractionId) {
        Optional<TouristAttraction> touristAttraction = touristAttractionRepository.findById(attractionId);
        if (touristAttraction.isPresent()) {
            List<Review> reviews = reviewRepository.findAllByTouristAttraction(touristAttraction.get());
            return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        return null;
    }

    private ReviewWithUserNameDTO convertToDTO(Review review) {
        User user = review.getUser();
        TouristAttraction touristAttraction = review.getTouristAttraction();
        return new ReviewWithUserNameDTO(
                review.getReviewId(),
                user.getName(),
                touristAttraction.getAttractionId(),
                review.getReviewText(),
                review.getRating(),
                review.getCreatedAt()
        );
    }
}
