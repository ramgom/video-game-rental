package com.ramgom.videogamerental.users.responses;

import com.ramgom.videogamerental.users.UserEntity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserResponse {
    Long id;
    String name;

    public static CreateUserResponse toCreateUserResponse(UserEntity userEntity) {
        return CreateUserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .build();
    }
}
