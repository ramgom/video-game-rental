package com.ramgom.videogamerental.game.exceptions;

import com.ramgom.videogamerental.game.GameEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DuplicateGameException extends RuntimeException {
    private final GameEntity game;
}
