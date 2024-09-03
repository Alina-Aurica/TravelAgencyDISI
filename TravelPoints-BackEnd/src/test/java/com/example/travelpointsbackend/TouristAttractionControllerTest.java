package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
import com.example.travelpointsbackend.model.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TouristAttractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String NAME = "Tourist Attraction";
    private static final String LOCATION = "Cluj";
    private static final String DESCRIPTION = "Test description";
    private static final String IMAGE_PATH = "test/image/path";
    private static final String OFFERS = "Test offers";
    private static final String URL = "http://localhost:8888//attractions";
    private static final float PRICE = 10.0f;


    private TouristAttractionDetailDTO createTouristAttractionDetailDTO() {
        TouristAttractionDetailDTO touristAttractionDetailDTO = new TouristAttractionDetailDTO();
        touristAttractionDetailDTO.setName(NAME);
        touristAttractionDetailDTO.setLocation(LOCATION);
        touristAttractionDetailDTO.setCategory(Category.NATURE.name());
        touristAttractionDetailDTO.setDescriptionText(DESCRIPTION);
        touristAttractionDetailDTO.setImagePath(IMAGE_PATH);
        touristAttractionDetailDTO.setEntryPrice(PRICE);
        touristAttractionDetailDTO.setOffers(OFFERS);
        return touristAttractionDetailDTO;
    }


    @Test
    @WithMockUser
    void testDeleteTouristAttractionSuccess() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(URL+"/delete/{id}",id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAddTouristAttractionSuccess() throws Exception {
        TouristAttractionDetailDTO touristAttractionDetailDTO = new TouristAttractionDetailDTO();

        mockMvc.perform(post(URL+"/addAttraction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(touristAttractionDetailDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteTouristAttractionInvalidDTO() throws Exception {
        Long id = 100L;

        mockMvc.perform(delete(URL+"/delete/{id}",id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testAddTouristAttractionInvalidDTO() throws Exception {
        TouristAttractionDetailDTO touristAttractionDetailDTO = new TouristAttractionDetailDTO();

        mockMvc.perform(post(URL+"/addAttraction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(touristAttractionDetailDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testUpdateTouristAttractionSuccess() throws Exception {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createTouristAttractionDetailDTO();
        Long id  = 2L;

        mockMvc.perform(put(URL+"/update/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(touristAttractionDetailDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testUpdateTouristAttractionNotFound() throws Exception {
        TouristAttractionDetailDTO touristAttractionDetailDTO = createTouristAttractionDetailDTO();
        Long id  = 10L;

        mockMvc.perform(put(URL+"/update/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(touristAttractionDetailDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindAllTouristAttractions() throws Exception {
        mockMvc.perform(get(URL+"/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}
