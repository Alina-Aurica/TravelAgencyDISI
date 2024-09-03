package com.example.travelpointsbackend.repository;

import com.example.travelpointsbackend.model.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristAttractionRepository extends JpaRepository<TouristAttraction, Long> {
}
