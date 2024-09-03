package com.example.travelpointsbackend.service.implementation;

import com.example.travelpointsbackend.dto.SocketMessageDTO;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.model.TouristAttraction;
import com.example.travelpointsbackend.model.User;
import com.example.travelpointsbackend.model.UserWishlist;
import com.example.travelpointsbackend.repository.TouristAttractionRepository;
import com.example.travelpointsbackend.repository.UserWishlistRepository;
import com.example.travelpointsbackend.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImplementation implements WebSocketService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final UserWishlistRepository userWishlistRepository;
    private final TouristAttractionRepository touristAttractionRepository;

    @Override
    public SocketMessageDTO verifyIfUpdateOffersAndUserLogged(Long attractionId, TouristAttractionDetailDTO touristAttractionDetailDTO) {
        Optional<TouristAttraction> touristAttractionOld = touristAttractionRepository.findById(attractionId);
        if(touristAttractionOld.isPresent()) {
            if (!touristAttractionOld.get().getAttractionDetail().getOffers().equals(touristAttractionDetailDTO.getOffers())) {
                List<UserWishlist> userWishlists = userWishlistRepository.findAll();
                for (UserWishlist userWishlist : userWishlists) {
                    List<TouristAttraction> touristAttractions = userWishlist.getTouristAttractionList();
                    if (touristAttractions.contains(touristAttractionOld.get())) {
                        User user = userWishlist.getUser();
                        if (user.isLogged()) {
                            Long userId = user.getUserId();
                            String attractionName = touristAttractionDetailDTO.getName();
                            String offers = touristAttractionDetailDTO.getOffers();
                            return new SocketMessageDTO(userId, attractionName, offers);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void sendMessage(SocketMessageDTO socketMessageDTO){
        Long userId = socketMessageDTO.getUserId();
        simpMessagingTemplate.convertAndSend("/topic/" + userId, socketMessageDTO);
    }
}
