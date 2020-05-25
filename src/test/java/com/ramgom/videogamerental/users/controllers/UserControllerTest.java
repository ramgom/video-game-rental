package com.ramgom.videogamerental.users.controllers;

import com.ramgom.videogamerental.VideoGameRentalApplication;
import com.ramgom.videogamerental.users.UserEntity;
import com.ramgom.videogamerental.users.UserService;
import com.ramgom.videogamerental.users.exceptions.DuplicateUserException;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import com.ramgom.videogamerental.users.requests.AddUserRequest;
import com.ramgom.videogamerental.users.responses.UserResponse;
import com.ramgom.videogamerental.users.responses.CreateUserResponse;
import com.ramgom.videogamerental.users.responses.UserDetailsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VideoGameRentalApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

    private static final Long USER_ID = 123L;
    private static final String USER_NAME = "name";
    private static final Integer LOYALTY_POINTS = 56;
    private static final CreateUserResponse CREATE_USER_RESPONSE = CreateUserResponse.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .build();
    private static final UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .build();
    private static final UserDetailsResponse USER_DETAILS_RESPONSE = UserDetailsResponse.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .loyaltyPoints(LOYALTY_POINTS)
            .build();
    private static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .build();
    private static final String ADD_USER_REQUEST_JSON = "{\"name\": \"name\"}";


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void createUser() throws Exception {
        when(userService.addUser(any(AddUserRequest.class))).thenReturn(CREATE_USER_RESPONSE);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(ADD_USER_REQUEST_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(USER_ID), Long.class))
                .andExpect(jsonPath("$.name", is(USER_NAME)));
    }

    @Test
    public void createUser_With_Duplicate_User() throws Exception {
        when(userService.addUser(any(AddUserRequest.class))).thenThrow(new DuplicateUserException(USER_ENTITY));

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(ADD_USER_REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(USER_ID), Long.class))
                .andExpect(jsonPath("$.name", is(USER_NAME)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"name\": \"\"}", "{}", "{\"name\": \"   \"}"})
    public void createUser_With_Invalid_User_Name(String request) throws Exception {
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getUser() throws Exception {
        when(userService.getUser(USER_ID)).thenReturn(USER_DETAILS_RESPONSE);

        mockMvc.perform(get("/users/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(USER_ID), Long.class))
                .andExpect(jsonPath("$.name", is(USER_NAME)))
                .andExpect(jsonPath("$.loyaltyPoints", is(LOYALTY_POINTS)));
    }

    @Test
    public void getUser_With_Invalid_User_Id() throws Exception {
        when(userService.getUser(USER_ID)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/" + USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void getUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.singletonList(USER_RESPONSE));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(is(USER_ID), Long.class))
                .andExpect(jsonPath("$[0].name", is(USER_NAME)));
    }
}