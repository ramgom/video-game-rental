package com.ramgom.videogamerental.rental;

import com.ramgom.videogamerental.price.PriceCalculatorService;
import com.ramgom.videogamerental.price.PriceService;
import com.ramgom.videogamerental.rental.exceptions.GameNotFoundException;
import com.ramgom.videogamerental.rental.exceptions.GameNotRentedException;
import com.ramgom.videogamerental.rental.exceptions.GameRentedException;
import com.ramgom.videogamerental.rental.request.RentalDetailsRequest;
import com.ramgom.videogamerental.rental.request.ReturnDetailsRequest;
import com.ramgom.videogamerental.rental.responses.ReturnResponse;
import com.ramgom.videogamerental.users.UserService;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameService;
import com.ramgom.videogamerental.rental.responses.RentalResponse;
import com.ramgom.videogamerental.rental.responses.UserRentalResponse;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RentalServiceTest {
    @Tested
    private RentalService rentalService;

    @Injectable
    private RentalRepository rentalRepository;

    @Injectable
    private GameService gameService;

    @Injectable
    private PriceCalculatorService priceCalculatorService;

    @Injectable
    private UserService userService;

    @Injectable
    private PriceService priceService;

    private RentalEntity rentalEntity1;

    private RentalEntity rentalEntity2;

    @BeforeEach
    public void setUp() {
        rentalEntity1 = RentalEntity.builder()
                .user(RentalServiceTestFixture.USER)
                .game(RentalServiceTestFixture.GAME1)
                .days(RentalServiceTestFixture.RENTAL_DAYS)
                .loyaltyPoints(RentalServiceTestFixture.LOYALTY_POINTS1)
                .rentalDate(LocalDate.now())
                .returned(false)
                .build();

        rentalEntity2 = RentalEntity.builder()
                .user(RentalServiceTestFixture.USER)
                .game(RentalServiceTestFixture.GAME2)
                .days(RentalServiceTestFixture.RENTAL_DAYS)
                .loyaltyPoints(RentalServiceTestFixture.LOYALTY_POINTS2)
                .rentalDate(RentalServiceTestFixture.REQUEST_DATE)
                .returned(false)
                .build();
    }
    @Test
    public void getRentals(@Mocked UserRentalResponse.RentalDetails rentalDetails) {
        new Expectations() {{
            userService.findUserById(RentalServiceTestFixture.USER_ID); result = RentalServiceTestFixture.USER;

            rentalRepository.findAllActiveRentalsByUser(RentalServiceTestFixture.USER); result = Collections.singletonList(rentalEntity1);
            UserRentalResponse.RentalDetails.toRentalDetails(rentalEntity1); result = rentalDetails;

            rentalRepository.findAllRentalsByUser(RentalServiceTestFixture.USER); times = 0;
        }};

        UserRentalResponse result = rentalService.getRentals(RentalServiceTestFixture.USER_ID, Boolean.FALSE);

        validateUserRentalResponse(result, rentalDetails);
    }

    @Test
    public void getRentals_With_Historical(@Mocked UserRentalResponse.RentalDetails rentalDetails) {
        new Expectations() {{
            userService.findUserById(RentalServiceTestFixture.USER_ID); result = RentalServiceTestFixture.USER;

            rentalRepository.findAllRentalsByUser(RentalServiceTestFixture.USER); result = Collections.singletonList(rentalEntity1);
            UserRentalResponse.RentalDetails.toRentalDetails(rentalEntity1); result = rentalDetails;

            rentalRepository.findAllActiveRentalsByUser(RentalServiceTestFixture.USER); times = 0;
        }};

        UserRentalResponse result = rentalService.getRentals(RentalServiceTestFixture.USER_ID, Boolean.TRUE);

        validateUserRentalResponse(result, rentalDetails);
    }

    private void validateUserRentalResponse(UserRentalResponse response, UserRentalResponse.RentalDetails... rentals) {
        assertThat(response.getUserName()).isEqualTo(RentalServiceTestFixture.USER_NAME);
        assertThat(response.getRentals()).containsOnly(rentals);
    }

    @Test
    public void getRentals_With_UserNotFound() {
        new Expectations() {{
            userService.findUserById(RentalServiceTestFixture.USER_ID); result = new UserNotFoundException();
        }};

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> rentalService.getRentals(RentalServiceTestFixture.USER_ID, Boolean.FALSE));
    }

    @Test
    public void rentGame(
            @Mocked RentalDetailsRequest rentalDetails1,
            @Mocked RentalDetailsRequest rentalDetails2,
            @Mocked RentalResponse.RentalResult rentalResult1,
            @Mocked RentalResponse.RentalResult rentalResult2
    ) {
        new Expectations() {{
            userService.findUserById(RentalServiceTestFixture.USER_ID); result = RentalServiceTestFixture.USER;

            rentalDetails1.getGameId(); result = RentalServiceTestFixture.GAME_ID1;
            rentalDetails1.getDays(); result = RentalServiceTestFixture.RENTAL_DAYS;
            rentalDetails1.getDate(); result = null;

            rentalDetails2.getGameId(); result = RentalServiceTestFixture.GAME_ID2;
            rentalDetails2.getDays(); result = RentalServiceTestFixture.RENTAL_DAYS;
            rentalDetails2.getDate(); result = RentalServiceTestFixture.REQUEST_DATE;

            gameService.getGame(RentalServiceTestFixture.GAME_ID1); result = RentalServiceTestFixture.GAME1;
            gameService.getGame(RentalServiceTestFixture.GAME_ID2); result = RentalServiceTestFixture.GAME2;

            priceCalculatorService.calculatePrice(RentalServiceTestFixture.GAME_TYPE1, RentalServiceTestFixture.RENTAL_DAYS); result = RentalServiceTestFixture.RENTAL_PRICE1;
            priceCalculatorService.calculatePrice(RentalServiceTestFixture.GAME_TYPE2, RentalServiceTestFixture.RENTAL_DAYS); result = RentalServiceTestFixture.RENTAL_PRICE2;

            priceService.getPrice(RentalServiceTestFixture.GAME_TYPE1); result = RentalServiceTestFixture.PRICE1;
            priceService.getPrice(RentalServiceTestFixture.GAME_TYPE2); result = RentalServiceTestFixture.PRICE2;

            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME1); result = Optional.empty();
            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME2); result = Optional.empty();

            rentalRepository.save((RentalEntity) any); returns(rentalEntity1, rentalEntity2);

            RentalResponse.RentalResult.toRentalResult(rentalEntity1, RentalServiceTestFixture.RENTAL_PRICE1); result = rentalResult1;
            RentalResponse.RentalResult.toRentalResult(rentalEntity2, RentalServiceTestFixture.RENTAL_PRICE2); result = rentalResult2;

            rentalResult1.getLoyaltyPoints(); result = RentalServiceTestFixture.LOYALTY_POINTS1;
            rentalResult1.getAmountInCents(); result = RentalServiceTestFixture.RENTAL_PRICE1;

            rentalResult2.getLoyaltyPoints(); result = RentalServiceTestFixture.LOYALTY_POINTS2;
            rentalResult2.getAmountInCents(); result = RentalServiceTestFixture.RENTAL_PRICE2;
        }};

        RentalResponse rentalResponse = rentalService.rentGame(RentalServiceTestFixture.USER_ID, Arrays.asList(rentalDetails1, rentalDetails2));

        assertThat(rentalResponse.getUserName()).isEqualTo(RentalServiceTestFixture.USER_NAME);
        assertThat(rentalResponse.getTotalAmountInCents()).isEqualTo(RentalServiceTestFixture.TOTAL_RENTAL_PRICE);
        assertThat(rentalResponse.getTotalLoyaltyPoints()).isEqualTo(RentalServiceTestFixture.TOTAL_LOYALTY_POINTS);
        assertThat(rentalResponse.getRentals()).hasSize(2);
        assertThat(rentalResponse.getRentals().get(0)).isSameAs(rentalResult1);
        assertThat(rentalResponse.getRentals().get(1)).isSameAs(rentalResult2);

        new FullVerifications(){{
            List<RentalEntity> entities = new ArrayList<>();
            rentalRepository.save(withCapture(entities));

            assertThat(entities).hasSize(2);
            assertThat(entities).containsOnly(rentalEntity1, rentalEntity2);
        }};
    }

    @Test
    public void rentGame_With_User_Not_Found() {
        new Expectations() {{
            userService.findUserById(RentalServiceTestFixture.USER_ID); result = new UserNotFoundException();
            rentalRepository.save((RentalEntity) any); times = 0;
        }};

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> rentalService.rentGame(RentalServiceTestFixture.USER_ID, Collections.emptyList()));
    }

    @Test
    public void rentGame_With_Game_Not_Found(@Mocked RentalDetailsRequest rentalDetails) {
        new Expectations() {{
            gameService.getGame(anyLong); result = new GameNotFoundException();
            rentalRepository.save((RentalEntity) any); times = 0;
        }};

        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> rentalService.rentGame(RentalServiceTestFixture.USER_ID, Collections.singletonList(rentalDetails)));
    }

    @Test
    public void rentGame_With_Game_Rented(@Mocked RentalDetailsRequest rentalDetails) {

        new Expectations() {{
            rentalDetails.getGameId(); result = RentalServiceTestFixture.GAME_ID1;
            gameService.getGame(RentalServiceTestFixture.GAME_ID1); result = RentalServiceTestFixture.GAME1;
            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME1); result = Optional.of(RentalServiceTestFixture.GAME1);
            rentalRepository.save((RentalEntity) any); times = 0;
        }};

        assertThatExceptionOfType(GameRentedException.class)
                .isThrownBy(() -> rentalService.rentGame(RentalServiceTestFixture.USER_ID, Collections.singletonList(rentalDetails)))
                .satisfies(ex -> assertThat(ex.getGameName()).isEqualTo(RentalServiceTestFixture.GAME_NAME1));
    }

    @Test
    public void returnRental(
            @Mocked ReturnDetailsRequest returnDetails1,
            @Mocked ReturnDetailsRequest returnDetails2,
            @Mocked ReturnResponse.ReturnResult returnResult1,
            @Mocked ReturnResponse.ReturnResult returnResult2
            ) {
        new Expectations() {{
            returnDetails1.getGameId(); result = RentalServiceTestFixture.GAME_ID1;
            returnDetails1.getDate(); result = null;

            returnDetails2.getGameId(); result = RentalServiceTestFixture.GAME_ID2;
            returnDetails2.getDate(); result = RentalServiceTestFixture.REQUEST_DATE;

            gameService.getGame(RentalServiceTestFixture.GAME_ID1); result = RentalServiceTestFixture.GAME1;
            gameService.getGame(RentalServiceTestFixture.GAME_ID2); result = RentalServiceTestFixture.GAME2;

            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME1); result = Optional.of(rentalEntity1);
            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME2); result = Optional.of(rentalEntity2);

            rentalRepository.save(rentalEntity1); result = rentalEntity1;
            rentalRepository.save(rentalEntity2); result = rentalEntity2;

            priceCalculatorService.calculateSurcharge(rentalEntity1); result = RentalServiceTestFixture.RENTAL_PRICE1;
            priceCalculatorService.calculateSurcharge(rentalEntity2); result = RentalServiceTestFixture.RENTAL_PRICE2;

            ReturnResponse.ReturnResult.toReturnResult(rentalEntity1, RentalServiceTestFixture.RENTAL_PRICE1); result = returnResult1;
            ReturnResponse.ReturnResult.toReturnResult(rentalEntity2, RentalServiceTestFixture.RENTAL_PRICE2); result = returnResult2;

            returnResult1.getSurchargeAmountInCents(); result = RentalServiceTestFixture.RENTAL_PRICE1;
            returnResult2.getSurchargeAmountInCents(); result = RentalServiceTestFixture.RENTAL_PRICE2;
        }};

        ReturnResponse returnResponse = rentalService.rentalReturns(Arrays.asList(returnDetails1, returnDetails2));

        assertThat(returnResponse.getTotalSurchargeAmountInCents()).isEqualTo(RentalServiceTestFixture.TOTAL_RENTAL_PRICE);
        assertThat(returnResponse.getReturns()).hasSize(2);
        assertThat(returnResponse.getReturns().get(0)).isSameAs(returnResult1);
        assertThat(returnResponse.getReturns().get(1)).isSameAs(returnResult2);
        assertThat(rentalEntity1.getReturned()).isTrue();
        assertThat(rentalEntity1.getReturnedDate()).isEqualTo(LocalDate.now());
        assertThat(rentalEntity2.getReturned()).isTrue();
        assertThat(rentalEntity2.getReturnedDate()).isEqualTo(RentalServiceTestFixture.REQUEST_DATE);
    }

    @Test
    public void returnRental_With_Game_Not_Found(@Mocked ReturnDetailsRequest returnDetails) {
        new Expectations() {{
            gameService.getGame(anyLong); result = new GameNotFoundException();
            rentalRepository.findNotReturnedByGame((GameEntity) any); times = 0;
            rentalRepository.save((RentalEntity) any); times = 0;
        }};

        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> rentalService.rentalReturns(Collections.singletonList(returnDetails)));
    }

    @Test
    public void returnRental_With_Game_Not_Rented(@Mocked ReturnDetailsRequest returnDetails) {
        new Expectations() {{
            returnDetails.getGameId(); result = RentalServiceTestFixture.GAME_ID1;
            gameService.getGame(RentalServiceTestFixture.GAME_ID1); result = RentalServiceTestFixture.GAME1;

            rentalRepository.findNotReturnedByGame(RentalServiceTestFixture.GAME1); result = Optional.empty();
            rentalRepository.save((RentalEntity) any); times = 0;
        }};

        assertThatExceptionOfType(GameNotRentedException.class)
                .isThrownBy(() -> rentalService.rentalReturns(Collections.singletonList(returnDetails)))
                .satisfies(ex -> assertThat(ex.getGameName()).isEqualTo(RentalServiceTestFixture.GAME_NAME1));
    }
}