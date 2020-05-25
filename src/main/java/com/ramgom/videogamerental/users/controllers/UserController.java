package com.ramgom.videogamerental.users.controllers;

import com.ramgom.videogamerental.users.requests.AddUserRequest;
import com.ramgom.videogamerental.users.UserService;
import com.ramgom.videogamerental.users.responses.CreateUserResponse;
import com.ramgom.videogamerental.users.responses.UserDetailsResponse;
import com.ramgom.videogamerental.users.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse createUser(@RequestBody @Valid AddUserRequest request) {
        log.info("operation=createUser, request={}", request);
        return userService.addUser(request);
    }

    @GetMapping("/{user_id}")
    public UserDetailsResponse getUser(@PathVariable("user_id") Long userId) {
        log.info("operation=getUser, userId={}", userId);
        return userService.getUser(userId);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        log.info("operation=getUser");
        return userService.getUsers();
    }
}
