package com.ramgom.videogamerental.game.requests;

import com.ramgom.videogamerental.game.GameType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateGameRequest {

    @NotBlank(message = "Game name can not be empty")
    private String name;

    @NotNull
    private GameType type;
}
