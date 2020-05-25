package com.ramgom.videogamerental.users;

import com.ramgom.videogamerental.rental.LoyaltyPointsService;
import com.ramgom.videogamerental.users.exceptions.DuplicateUserException;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import com.ramgom.videogamerental.users.requests.AddUserRequest;
import com.ramgom.videogamerental.users.responses.CreateUserResponse;
import com.ramgom.videogamerental.users.responses.UserDetailsResponse;
import com.ramgom.videogamerental.users.responses.UserResponse;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserServiceTest {

    private static final Long USER_ID = 123L;
    private static final String USER_NAME = "name";
    private static final Integer LOYALTY_POINTS = 456;
    private static final UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .build();

    @Tested
    private UserService userService;

    @Injectable
    private UserRepository userRepository;

    @Injectable
    private LoyaltyPointsService loyaltyPointsService;

    @Test
    public void getUsers() {
        new Expectations() {{
            userRepository.findAll(); result = Collections.singletonList(USER_ENTITY);
        }};

        List<UserResponse> users = userService.getUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo(USER_ID);
        assertThat(users.get(0).getName()).isEqualTo(USER_NAME);
    }

    @Test
    public void findUserById() {
        new Expectations() {{
            userRepository.findById(USER_ID); result = Optional.of(USER_ENTITY);
        }};

        UserEntity result = userService.findUserById(USER_ID);

        assertThat(result).isSameAs(USER_ENTITY);

        new FullVerifications() {{}};
    }

    @Test
    public void findUserById_With_User_Not_Found() {
        new Expectations() {{
            userRepository.findById(USER_ID); result = Optional.empty();
        }};

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findUserById(USER_ID));

        new FullVerifications() {{}};
    }

    @Test
    public void getUser() {
        new Expectations(userService) {{
            userService.findUserById(USER_ID); result = USER_ENTITY;

            loyaltyPointsService.getLoyaltyPoints(USER_ID); result = LOYALTY_POINTS;
        }};

        UserDetailsResponse user = userService.getUser(USER_ID);

        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getName()).isEqualTo(USER_NAME);
        assertThat(user.getLoyaltyPoints()).isEqualTo(LOYALTY_POINTS);

        new FullVerifications() {{}};
    }

    @Test
    public void addUser(@Mocked AddUserRequest addUserRequest, @Mocked CreateUserResponse createUserResponse) {
        new Expectations() {{
            addUserRequest.getName(); result = USER_NAME;
            userRepository.save((UserEntity) any); result = USER_ENTITY;

            userRepository.findByName(USER_NAME); times = 0;

            CreateUserResponse.toCreateUserResponse(USER_ENTITY); result = createUserResponse;
        }};

        CreateUserResponse result = userService.addUser(addUserRequest);

        assertThat(result).isSameAs(createUserResponse);

        new FullVerifications() {{
            UserEntity capturedUserEntity;

            userRepository.save(capturedUserEntity = withCapture());

            assertThat(capturedUserEntity.getName()).isEqualTo(USER_NAME);
        }};
    }

    @Test
    public void addUser_With_Duplicate_User(@Mocked AddUserRequest addUserRequest) {
        new Expectations() {{
            addUserRequest.getName(); result = USER_NAME;
            userRepository.save((UserEntity) any); result = new DataIntegrityViolationException("Error");

            userRepository.findByName(USER_NAME); result = USER_ENTITY;
        }};

        assertThatExceptionOfType(DuplicateUserException.class)
                .isThrownBy(() -> userService.addUser(addUserRequest))
                .satisfies(ex -> Assertions.assertThat(ex.getUser()).isSameAs(USER_ENTITY));

        new FullVerifications() {{}};
    }
}