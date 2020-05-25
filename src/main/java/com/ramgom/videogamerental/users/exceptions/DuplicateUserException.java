package com.ramgom.videogamerental.users.exceptions;

import com.ramgom.videogamerental.users.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DuplicateUserException extends RuntimeException {
    private final UserEntity user;
}
