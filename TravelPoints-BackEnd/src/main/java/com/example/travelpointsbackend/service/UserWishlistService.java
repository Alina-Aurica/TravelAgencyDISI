package com.example.travelpointsbackend.service;


import com.example.travelpointsbackend.dto.UserWishlistDTO;


public interface UserWishlistService {

    public boolean addTouristAttraction(Long touristAttractionId,Long userId);
    public UserWishlistDTO getUserWishlist(Long userId);
}
