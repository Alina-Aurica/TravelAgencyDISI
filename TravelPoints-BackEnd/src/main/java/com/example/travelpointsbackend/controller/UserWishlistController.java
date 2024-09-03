package com.example.travelpointsbackend.controller;

import com.example.travelpointsbackend.dto.UserWishlistDTO;
import com.example.travelpointsbackend.service.UserWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class UserWishlistController {

    private static final String ADD_FAILED = "{\"message\": \"Add tourist attraction failed!\"}";
    private static final String ADD_SUCCESS = "{\"message\": \"Add tourist attraction success!\"}";
    private static final String GET_FAILED = "{\"message\": \"Get wishlist failed!\"}";

    @Autowired
    private UserWishlistService userWishlistService;

    @PreAuthorize("hasAuthority('TOURIST')")
    @PostMapping("/addTouristAttraction/{userId}/{touristAttractionId}")
    public ResponseEntity addTouristAttraction(@PathVariable Long userId, @PathVariable Long touristAttractionId) {
        if (userWishlistService.addTouristAttraction(touristAttractionId,userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(ADD_SUCCESS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ADD_FAILED);
    }

    @PreAuthorize("hasAuthority('TOURIST')")
    @GetMapping("/getWishlist/{userId}")
    public ResponseEntity getUserWishlist(@PathVariable Long userId) {
        UserWishlistDTO userWishlistDTO = userWishlistService.getUserWishlist(userId);
        if(Objects.isNull(userWishlistDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GET_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userWishlistDTO);
    }

}
