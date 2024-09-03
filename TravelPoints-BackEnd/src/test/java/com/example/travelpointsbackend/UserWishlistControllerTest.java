package com.example.travelpointsbackend;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserWishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "http://localhost:8888//wishlist";
    private static final Long ID_USER  = 2L;
    private static final Long ID_ATTRACTION  = 5L;
    private static final String ADD_FAILED = "{\"message\": \"Add tourist attraction failed!\"}";
    private static final String ADD_SUCCESS = "{\"message\": \"Add tourist attraction success!\"}";
    private static final String GET_FAILED = "{\"message\": \"Get wishlist failed!\"}";


    @Test
    @WithMockUser(authorities = {"TOURIST"})
    public void testAddAttractionSuccess() throws Exception {
        mockMvc.perform(post(URL+"/addTouristAttraction/{userId}/{touristAttractionId}",ID_USER,ID_ATTRACTION))
                .andExpect(status().isOk())
                .andExpect(content().string(ADD_SUCCESS));
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    public void testAddAttractionFailure() throws Exception {
        mockMvc.perform(post(URL+"/addTouristAttraction/{userId}/{touristAttractionId}",ID_ATTRACTION,ID_ATTRACTION))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ADD_FAILED));
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void testAddAttractionWrongAuthorities() throws Exception {
        mockMvc.perform(post(URL+"/addTouristAttraction/{userId}/{touristAttractionId}",ID_ATTRACTION,ID_ATTRACTION))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    public void testGetWishlistSuccess() throws Exception {

        mockMvc.perform(get(URL+"/getWishlist/{userId}",ID_USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wishlistId").exists())
                .andExpect(jsonPath("$.attractionsIds").exists())
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    @WithMockUser(authorities = {"TOURIST"})
    public void testGetWishlistFailure() throws Exception {
        mockMvc.perform(get(URL+"/getWishlist/{userId}",ID_ATTRACTION))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(GET_FAILED));
    }

    @Test
    @WithMockUser(authorities = {"ADMINISTRATOR"})
    public void testGetWishlistWrongAuthorities() throws Exception {
        mockMvc.perform(get(URL+"/getWishlist/{userId}",ID_USER))
                .andExpect(status().isForbidden());
    }



}
