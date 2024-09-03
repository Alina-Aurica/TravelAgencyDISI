package com.example.travelpointsbackend.repository;

import com.example.travelpointsbackend.model.VisitStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticRepository extends JpaRepository<VisitStatistic, Long> {
}
