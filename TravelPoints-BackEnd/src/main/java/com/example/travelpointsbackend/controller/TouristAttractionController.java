package com.example.travelpointsbackend.controller;

import com.example.travelpointsbackend.dto.SocketMessageDTO;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.service.TouristAttractionService;
import com.example.travelpointsbackend.service.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class TouristAttractionController {

    @Autowired
    TouristAttractionService touristAttractionService;
    @Autowired
    WebSocketService webSocketService;

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/create")
    public ResponseEntity addAttraction(@Valid @RequestBody TouristAttractionDetailDTO touristAttractionDetailDTO) {
        return ResponseEntity.ok(touristAttractionService.addTouristAttraction(touristAttractionDetailDTO));
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAttraction(@PathVariable Long id) {
        boolean isDeleted = touristAttractionService.deleteTouristAttraction(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAttraction(@PathVariable Long id, @Valid @RequestBody TouristAttractionDetailDTO touristAttractionDetailDTO) {
        SocketMessageDTO socketMessageDTO = webSocketService.verifyIfUpdateOffersAndUserLogged(id, touristAttractionDetailDTO);
        if (!Objects.isNull(socketMessageDTO)){
            webSocketService.sendMessage(socketMessageDTO);
        }
        boolean isUpdated = touristAttractionService.updateTouristAttraction(id, touristAttractionDetailDTO);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TouristAttractionDetailDTO>> getAllAttractions() {
        List<TouristAttractionDetailDTO> attractions = touristAttractionService.findAllAttractions();
        return ResponseEntity.ok(attractions);
    }
}
