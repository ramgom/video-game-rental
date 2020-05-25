package com.ramgom.videogamerental.users.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    Long id;
    String name;
}
