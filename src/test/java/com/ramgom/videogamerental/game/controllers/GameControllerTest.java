package com.ramgom.videogamerental.game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramgom.videogamerental.VideoGameRentalApplication;
import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameService;
import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.game.exceptions.DuplicateGameException;
import com.ramgom.videogamerental.game.requests.CreateGameRequest;
import com.ramgom.videogamerental.game.responses.GameResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Disabled
@SpringBootTest(classes = VideoGameRentalApplication.class)
@AutoConfigureMockMvc
class GameControllerTest {

    private static final Long GAME_ID = 123L;
    private static final String GAME_NAME = "name";
    private static final GameType GAME_TYPE = GameType.CLASSIC;
    private static final GameResponse GAME_RESPONSE = GameResponse.builder()
            .id(GAME_ID)
            .name(GAME_NAME)
            .type(GAME_TYPE)
            .build();

    private static final GameEntity GAME_ENTITY = GameEntity.builder()
            .id(GAME_ID)
            .name(GAME_NAME)
            .type(GAME_TYPE)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @MockBean
    private CreateGameRequest createGameRequest;

    @Test
    public void getAllGames() throws Exception {
        when(gameService.getGames()).thenReturn(List.of(GAME_RESPONSE));
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(is(GAME_ID), Long.class))
                .andExpect(jsonPath("$[0].name", is(GAME_NAME)))
                .andExpect(jsonPath("$[0].type", is(GAME_TYPE.name())));
    }

    @Test
    public void createGame() throws Exception {
        CreateGameRequest request = CreateGameRequest.builder().name(GAME_NAME).type(GAME_TYPE).build();

        when(gameService.addGame(any(CreateGameRequest.class))).thenReturn(GAME_RESPONSE);

        mockMvc.perform(post("/games").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(GAME_ID), Long.class))
                .andExpect(jsonPath("$.name", is(GAME_NAME)))
                .andExpect(jsonPath("$.type", is(GAME_TYPE.name())));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"type\": \"CLASSIC\"}",
            "{\"name\": \"\", \"type\": \"CLASSIC\"}",
            "{\"name\": \"name\"}",
            "{\"name\": \"name\", \"type\": \"INVALID\"}",
    })
    public void createGame_With_Errors(String request) throws Exception {
        mockMvc.perform(post("/games").content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void createGame_With_Duplicate_Game() throws Exception {
        CreateGameRequest request = CreateGameRequest.builder().name(GAME_NAME).type(GAME_TYPE).build();

        when(gameService.addGame(any(CreateGameRequest.class))).thenThrow(new DuplicateGameException(GAME_ENTITY));

        mockMvc.perform(post("/games").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(GAME_ID), Long.class))
                .andExpect(jsonPath("$.name", is(GAME_NAME)))
                .andExpect(jsonPath("$.type", is(GAME_TYPE.name())));
    }
}