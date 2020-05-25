package com.ramgom.videogamerental.rental.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GameRentedException extends RuntimeException {
    private final String gameName;
}
