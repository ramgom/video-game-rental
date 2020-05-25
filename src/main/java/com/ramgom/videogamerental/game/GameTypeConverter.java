package com.ramgom.videogamerental.game;

import org.springframework.core.convert.converter.Converter;

class GameTypeConverter implements Converter<String, GameType> {
    @Override
    public GameType convert(String source) {
        return GameType.valueOf(source.toUpperCase());
    }
}
