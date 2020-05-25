package com.ramgom.videogamerental.game.responses;

import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameResponseTest {

    private static final Long GAME_ID = 123L;
    private static final String GAME_NAME = "name";
    private static final GameType GAME_TYPE = GameType.STANDARD;

    @Test
    public void toGameResponse() {
        GameEntity game = GameEntity.builder()
                .id(GAME_ID)
                .name(GAME_NAME)
                .type(GAME_TYPE)
                .build();
        GameResponse response = GameResponse.toGameResponse(game);

        assertThat(response.getId()).isEqualTo(GAME_ID);
        assertThat(response.getName()).isEqualTo(GAME_NAME);
        assertThat(response.getType()).isEqualTo(GAME_TYPE);
    }
}