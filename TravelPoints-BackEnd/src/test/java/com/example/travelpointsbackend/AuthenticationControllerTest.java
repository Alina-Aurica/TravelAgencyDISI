package com.example.travelpointsbackend;

import com.example.travelpointsbackend.dto.UserDTO;
import com.example.travelpointsbackend.model.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String NAME = "Alina";

    private static final String EMAIL1 = "clau@gmail.com";
    private static final String EMAIL2 = "alina@gmail.com";

    private static final String EMAIL_INVALID ="invalid";
    private static final String DUPLICATE_EMAIL = "Duplicate email!";
    private static final Long ID = 1L;

    private static final String PASSWORD1 = "12345678";
    private static final String PASSWORD2 = "alinabanana";
    private static final String BAD_CREDENTIALS = "Bad credentials!";
    private static final String URL = "http://localhost:8888/auth";
    private static final String CHECK_LOGOUT_FAIL = "LogOut - still logged!";
    private static final String CHECK_LOGOUT_SUCCESS = "LogOut - not logged!";

    @Test
    void testLoginSuccess() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL2, PASSWORD2, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginFailure() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL1, PASSWORD1, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(BAD_CREDENTIALS));
    }

    @Test
    void testRegisterTouristSuccess() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL1, PASSWORD2, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testRegisterTouristInvalidEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL_INVALID, PASSWORD2, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTouristDuplicateEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL2, PASSWORD2, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(DUPLICATE_EMAIL));
    }

    @Test
    void testRegisterTouristInvalidPassword() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL1, PASSWORD1, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterTouristNoPasswordAndEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID,StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, UserType.TOURIST, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterAdministratorSuccess() throws Exception {
        UserDTO userDTO = new UserDTO(ID,NAME, EMAIL1,PASSWORD2, UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/registerAdministrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testRegisterAdministratorInvalidEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL_INVALID,PASSWORD2, UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/registerAdministrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterAdministratorDuplicateEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL2,PASSWORD2, UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/registerAdministrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(DUPLICATE_EMAIL));
    }

    @Test
    void testRegisterAdministratorInvalidPassword() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, EMAIL1, PASSWORD1, UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/registerAdministrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterAdministratorNoPasswordAndEmail() throws Exception {
        UserDTO userDTO = new UserDTO(ID, NAME, StringUtils.EMPTY,StringUtils.EMPTY, UserType.ADMINISTRATOR, new Timestamp(System.currentTimeMillis()),false);

        mockMvc.perform(post(URL+"/registerAdministrator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        mockMvc.perform(put(URL+"/logout/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(CHECK_LOGOUT_SUCCESS));
    }

    @Test
    public void testLogoutFailure() throws Exception {
        mockMvc.perform(put(URL+"/logout/2"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(CHECK_LOGOUT_FAIL));
    }



}