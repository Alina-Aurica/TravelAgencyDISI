package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.dto.UserWishlistDTO;
import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.model.UserWishlist;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserRepository;
import com.example.travelpointsbackend.repository.UserWishlistRepository;
import com.example.travelpointsbackend.service.UserWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserWishlistServiceImplementation implements UserWishlistService {


    private final UserWishlistRepository userWishlistRepository;
    private final UserRepository userRepository;
    private final TouristAttractionRepository touristAttractionRepository;
    private final SecurityExpressionHandler webSecurityExpressionHandler;

    @Override
    public boolean addTouristAttraction(Long touristAttractionId,Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<TouristAttraction> touristAttraction = touristAttractionRepository.findById(touristAttractionId);
        if(user.isPresent() && touristAttraction.isPresent()){
            User currentUser = user.get();
            TouristAttraction currentTouristAttraction = touristAttraction.get();
            UserWishlist userWishlist;
            if(currentUser.getUserWishlist()==null){
                userWishlist = UserWishlist.builder()
                        .addedAt(new Timestamp(System.currentTimeMillis()))
                        .user(currentUser)
                        .touristAttractionList(new ArrayList<>())
                        .build();
                currentUser.setUserWishlist(userWishlist);
                currentTouristAttraction.setUserWishlistList(new ArrayList<>());
                currentTouristAttraction.getUserWishlistList().add(userWishlist);
                userWishlist.getTouristAttractionList().add(currentTouristAttraction);
                userWishlistRepository.save(userWishlist);
                return true;
            }
            else{
                userWishlist = currentUser.getUserWishlist();
                if(currentTouristAttraction.getUserWishlistList()==null){
                    currentTouristAttraction.setUserWishlistList(new ArrayList<>());
                }
                if(currentTouristAttraction.getUserWishlistList().contains(userWishlist)){
                    return false;
                }
                currentTouristAttraction.getUserWishlistList().add(userWishlist);
                userWishlist.getTouristAttractionList().add(currentTouristAttraction);
                userWishlistRepository.save(userWishlist);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserWishlistDTO getUserWishlist(Long userId) {
        Optional<User>  user = userRepository.findById(userId);
        if(user.isPresent()){
            User currentUser = user.get();
            UserWishlist userWishlist = currentUser.getUserWishlist();
            if(userWishlist!=null){
                List<TouristAttraction> touristAttractionList = userWishlist.getTouristAttractionList();
                List<Long> attractionIDs = touristAttractionList.stream().map(TouristAttraction::getAttractionId).toList();
                UserWishlistDTO userWishlistDTO = new UserWishlistDTO();
                userWishlistDTO.setUserId(currentUser.getUserId());
                userWishlistDTO.setAddedAt(userWishlist.getAddedAt());
                userWishlistDTO.setAttractionsIds(attractionIDs);
                userWishlistDTO.setWishlistId(userWishlist.getWishlistId());
                return userWishlistDTO;

            }
            return null;
        }
        return null;
    }
}
