package com.example.travelpointsbackend.repository;

import com.example.travelpointsbackend.model.AttractionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionDetailRepository extends JpaRepository<AttractionDetail, Long> {

}
