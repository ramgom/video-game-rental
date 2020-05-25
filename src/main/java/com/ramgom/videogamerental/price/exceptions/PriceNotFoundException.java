package com.ramgom.videogamerental.price.exceptions;

import com.ramgom.videogamerental.game.GameType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PriceNotFoundException extends RuntimeException {
    private final GameType gameType;
}
