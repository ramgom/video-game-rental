package com.ramgom.videogamerental.game.controllers;

import com.ramgom.videogamerental.game.exceptions.DuplicateGameException;
import com.ramgom.videogamerental.game.responses.GameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameControllerAdvice {

    @ExceptionHandler(DuplicateGameException.class)
    @ResponseStatus(HttpStatus.OK)
    public GameResponse handleDuplicateGame(DuplicateGameException ex) {
        return GameResponse.toGameResponse(ex.getGame());
    }

}
