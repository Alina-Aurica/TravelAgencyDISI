package com.example.travelpointsbackend.service;

import com.example.travelpointsbackend.dto.ReviewDTO;
import com.example.travelpointsbackend.dto.ReviewWithUserNameDTO;
import com.example.travelpointsbackend.model.Review;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ReviewService {
    Optional<Review> addReview(ReviewDTO reviewDTO);
    boolean deleteReview(Long id);
    boolean updateReview(Long id, ReviewDTO reviewDTO);
    List<ReviewWithUserNameDTO> findAllReviewByTouristAttractionId(Long attractionId);
}
