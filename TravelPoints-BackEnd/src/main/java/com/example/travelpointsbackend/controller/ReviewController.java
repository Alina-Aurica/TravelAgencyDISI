package com.example.travelpointsbackend.controller;

import com.example.travelpointsbackend.dto.ReviewDTO;
import com.example.travelpointsbackend.dto.ReviewWithUserNameDTO;
import com.example.travelpointsbackend.model.Review;
import com.example.travelpointsbackend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private static final String ADD_FAILED = "{\"message\": \"Add review failed!\"}";
    private static final String DELETE_SUCCEED = "{\"message\": \"Delete review succeed!\"}";
    private static final String DELETE_FAILED = "{\"message\": \"Delete review failed!\"}";
    private static final String UPDATE_SUCCEED = "{\"message\": \"Update review succeed!\"}";
    private static final String UPDATE_FAILED = "{\"message\": \"Update review failed!\"}";
    private static final String GET_FAILED = "{\"message\": \"Get reviews failed!\"}";

    @Autowired
    ReviewService reviewService;

    @PreAuthorize("hasAuthority('TOURIST')")
    @PostMapping("/create")
    public ResponseEntity addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        Optional<Review> review = reviewService.addReview(reviewDTO);
        if (review.isEmpty()){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ADD_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    @PreAuthorize("hasAuthority('TOURIST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReview(@PathVariable Long id) {
        boolean isDeleted = reviewService.deleteReview(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCEED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DELETE_FAILED);
        }
    }

    @PreAuthorize("hasAuthority('TOURIST')")
    @PutMapping("/update/{id}")
    public ResponseEntity updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        boolean isUpdated = reviewService.updateReview(id, reviewDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(UPDATE_SUCCEED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UPDATE_FAILED);
        }
    }

    @GetMapping("/getAllByAttractionId/{attractionId}")
    public ResponseEntity getAllReviews(@PathVariable Long attractionId) {
        List<ReviewWithUserNameDTO> reviews = reviewService.findAllReviewByTouristAttractionId(attractionId);
        if(Objects.isNull(reviews)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GET_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}
