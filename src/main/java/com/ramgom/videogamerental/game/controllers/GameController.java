package com.ramgom.videogamerental.game.controllers;

import com.ramgom.videogamerental.game.GameService;
import com.ramgom.videogamerental.game.requests.CreateGameRequest;
import com.ramgom.videogamerental.game.responses.GameResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
@Slf4j
public class GameController {

    private final GameService gameService;

    @GetMapping
    public List<GameResponse> getAllGames() {
        log.info("method=getAllGames");
        return gameService.getGames();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@RequestBody @Valid CreateGameRequest request) {
        log.info("method=createGame, request={}", request);
        return gameService.addGame(request);
    }
}
