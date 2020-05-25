package com.ramgom.videogamerental.game.responses;

import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameResponse {
    Long id;
    String name;
    GameType type;

    static public GameResponse toGameResponse(GameEntity gameEntity) {
        return GameResponse.builder()
                .id(gameEntity.getId())
                .name(gameEntity.getName())
                .type(gameEntity.getType())
                .build();
    }
}
