package com.ramgom.videogamerental.users.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddUserRequest {

    @NotBlank(message = "User's name should not be empty")
    private String name;
}
