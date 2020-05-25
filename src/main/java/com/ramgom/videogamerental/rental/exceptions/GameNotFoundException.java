package com.ramgom.videogamerental.rental.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
