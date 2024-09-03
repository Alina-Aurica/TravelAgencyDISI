package com.example.travelpointsbackend.service;

import com.example.travelpointsbackend.dto.SocketMessageDTO;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import org.springframework.stereotype.Component;

@Component
public interface WebSocketService {
    SocketMessageDTO verifyIfUpdateOffersAndUserLogged(Long attractionId, TouristAttractionDetailDTO touristAttractionDetailDTO);
    void sendMessage(SocketMessageDTO socketMessageDTO);
}
