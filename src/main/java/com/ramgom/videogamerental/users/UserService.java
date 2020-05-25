package com.ramgom.videogamerental.users;

import com.ramgom.videogamerental.users.exceptions.DuplicateUserException;
import com.ramgom.videogamerental.users.requests.AddUserRequest;
import com.ramgom.videogamerental.rental.LoyaltyPointsService;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import com.ramgom.videogamerental.users.responses.CreateUserResponse;
import com.ramgom.videogamerental.users.responses.UserDetailsResponse;
import com.ramgom.videogamerental.users.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoyaltyPointsService loyaltyPointsService;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder().id(user.getId()).name(user.getName()).build())
                .collect(Collectors.toList());
    }

    public UserDetailsResponse getUser(Long userId) {
        UserEntity userEntity = findUserById(userId);
        return UserDetailsResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .loyaltyPoints(loyaltyPointsService.getLoyaltyPoints(userId))
                .build();
    }

    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public CreateUserResponse addUser(AddUserRequest addUserRequest) {
        try {
            UserEntity userEntity = UserEntity.builder().name(addUserRequest.getName()).build();
            UserEntity savedUserEntity = userRepository.save(userEntity);
            return CreateUserResponse.toCreateUserResponse(savedUserEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUserException(userRepository.findByName(addUserRequest.getName()).get());
        }
    }
}
