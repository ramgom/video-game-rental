package com.ramgom.videogamerental.game;

import mockit.Tested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTypeConverterTest {

    private final static GameType GAME_TYPE = GameType.NEW_RELEASE;

    @Tested
    private GameTypeConverter gameTypeConverter;

    @Test
    public void convert() {
        GameType result = gameTypeConverter.convert(GAME_TYPE.name().toLowerCase());

        assertThat(result).isEqualTo(GAME_TYPE);
    }
}