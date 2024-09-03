package com.example.travelpointsbackend.repository;

import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.model.UserWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {

}
