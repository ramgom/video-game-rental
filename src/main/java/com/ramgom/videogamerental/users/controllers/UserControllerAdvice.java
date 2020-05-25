package com.ramgom.videogamerental.users.controllers;

import com.ramgom.videogamerental.users.exceptions.DuplicateUserException;
import com.ramgom.videogamerental.ResponseError;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import com.ramgom.videogamerental.users.responses.CreateUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.OK)
    public CreateUserResponse handleDuplicateUser(DuplicateUserException ex) {
        return CreateUserResponse.toCreateUserResponse(ex.getUser());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleUserNotFound() {
        return ResponseError.builder().message("Invalid user id").build();
    }
}
