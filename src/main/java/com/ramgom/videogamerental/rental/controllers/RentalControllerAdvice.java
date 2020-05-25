package com.ramgom.videogamerental.rental.controllers;

import com.ramgom.videogamerental.ResponseError;
import com.ramgom.videogamerental.price.exceptions.PriceNotFoundException;
import com.ramgom.videogamerental.rental.exceptions.GameNotFoundException;
import com.ramgom.videogamerental.rental.exceptions.GameNotRentedException;
import com.ramgom.videogamerental.rental.exceptions.GameRentedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;

@RestControllerAdvice
public class RentalControllerAdvice {
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleGameNotFound() {
        return ResponseError.builder().message("Invalid game id").build();
    }

    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNoPriceFound(PriceNotFoundException ex) {
        return ResponseError.builder()
                .message(MessageFormat.format("No price has been defined for game type {0}", ex.getGameType()))
                .build();
    }

    @ExceptionHandler(GameRentedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleGameRented(GameRentedException ex) {
        return ResponseError.builder()
                .message(MessageFormat.format("Game \"{0}\" is not available for rent", ex.getGameName()))
                .build();
    }

    @ExceptionHandler(GameNotRentedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNotRentedGame(GameNotRentedException ex) {
        return ResponseError.builder()
                .message(MessageFormat.format("Game \"{0}\" has not been rented", ex.getGameName()))
                .build();
    }
}
