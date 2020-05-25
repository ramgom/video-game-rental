package com.ramgom.videogamerental;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseError {
    private final String message;
}
