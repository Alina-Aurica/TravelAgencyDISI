package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.ReviewDTO;
import com.example.travelpointsbackend.dto.TouristAttractionDetailDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "http://localhost:8888//reviews";
    private static final Long USER_ID = 1L;
    private static final Long ATTRACTION_ID = 1L;
    private static final String REVIEW_TEXT = "good";
    private static final Integer RATING = 3;

    private ReviewDTO createReviewDTO() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(USER_ID);
        reviewDTO.setAttractionId(ATTRACTION_ID);
        reviewDTO.setReviewText(REVIEW_TEXT);
        reviewDTO.setRating(RATING);
        return reviewDTO;
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testAddReviewSuccess() throws Exception {
        ReviewDTO reviewDTO = createReviewDTO();

        mockMvc.perform(post(URL+"/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testAddReviewFailed() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();

        mockMvc.perform(post(URL+"/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testUpdateReviewSuccess() throws Exception {
        ReviewDTO reviewDTO = createReviewDTO();
        Long id  = 3L;

        mockMvc.perform(put(URL+"/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testUpdateReviewFailed() throws Exception {
        ReviewDTO reviewDTO = createReviewDTO();
        Long id  = 10L;

        mockMvc.perform(put(URL+"/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testDeleteReviewSuccess() throws Exception {
        Long id = 4L;

        mockMvc.perform(delete(URL+"/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testDeleteReviewFailed() throws Exception {
        Long id = 10L;

        mockMvc.perform(delete(URL+"/delete/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    void testFindAllReviewsSuccess() throws Exception {
        Long attractionId = 1L;
        mockMvc.perform(get(URL+"/getAllByAttractionId/{attractionId}", attractionId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void testFindAllReviewsFailed() throws Exception {
        Long attractionId = 10L;
        mockMvc.perform(get(URL+"/getAllByAttractionId/{attractionId}", attractionId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }
}
