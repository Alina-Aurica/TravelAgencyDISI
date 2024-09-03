package com.example.travelpointsbackend.repository;

import com.example.travelpointsbackend.model.Review;
import com.example.travelpointsbackend.model.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByTouristAttraction(TouristAttraction touristAttraction);
}
