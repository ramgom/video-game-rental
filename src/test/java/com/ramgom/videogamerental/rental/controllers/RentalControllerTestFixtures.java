package com.ramgom.videogamerental.rental.controllers;

import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.rental.responses.ReturnResponse;
import com.ramgom.videogamerental.rental.request.RentalDetailsRequest;
import com.ramgom.videogamerental.rental.request.ReturnDetailsRequest;
import com.ramgom.videogamerental.rental.responses.RentalResponse;
import com.ramgom.videogamerental.rental.responses.UserRentalResponse;

import java.time.LocalDate;
import java.util.Collections;

public class RentalControllerTestFixtures {

    static final Long USER_ID = 123L;
    static final String USER_NAME = "user name";

    static final Long GAME_ID = 456L;
    static final String GAME_NAME = "name";
    static final GameType GAME_TYPE = GameType.CLASSIC;

    static final Integer RENTAL_DAYS = 10;
    static final Long RENTAL_ID = 789L;
    static final LocalDate RENTAL_DATE = LocalDate.now();
    static final LocalDate RETURN_DATE = RENTAL_DATE.plusDays(RENTAL_DAYS);
    static final LocalDate RETURNED_DATE = RENTAL_DATE.plusDays(15);

    static final Long TOTAL_AMOUNT_IN_CENTS = 100L;
    static final Integer TOTAL_LOYALTY_POINTS = 50;

    static final RentalDetailsRequest RENTAL_DETAILS_REQUEST = RentalDetailsRequest.builder()
            .gameId(GAME_ID)
            .days(RENTAL_DAYS)
            .build();

    static final RentalResponse.RentalResult RENTAL_RESULT = RentalResponse.RentalResult.builder()
            .id(RENTAL_ID)
            .gameName(GAME_NAME)
            .loyaltyPoints(TOTAL_LOYALTY_POINTS)
            .amountInCents(TOTAL_AMOUNT_IN_CENTS)
            .rentalDate(RENTAL_DATE)
            .returnDate(RETURN_DATE)
            .build();

    static final RentalResponse RENTAL_RESPONSE = RentalResponse.builder()
            .userName(USER_NAME)
            .rentals(Collections.singletonList(RENTAL_RESULT))
            .totalAmountInCents(TOTAL_AMOUNT_IN_CENTS)
            .totalLoyaltyPoints(TOTAL_LOYALTY_POINTS)
            .build();

    static final UserRentalResponse.RentalDetails RENTAL_DETAILS = UserRentalResponse.RentalDetails.builder()
            .id(RENTAL_ID)
            .gameName(GAME_NAME)
            .returnedDate(RETURNED_DATE)
            .rentalDate(RENTAL_DATE)
            .initialReturnDate(RETURN_DATE)
            .build();

    static final UserRentalResponse USER_RENTAL_RESPONSE = UserRentalResponse.builder()
            .userName(USER_NAME)
            .rentals(Collections.singletonList(RENTAL_DETAILS))
            .build();

    static final ReturnDetailsRequest RETURN_DETAILS_REQUEST = ReturnDetailsRequest.builder()
            .gameId(GAME_ID)
            .build();

    static final ReturnResponse.ReturnResult RETURN_RESULT = ReturnResponse.ReturnResult.builder()
            .id(RENTAL_ID)
            .gameName(GAME_NAME)
            .returnedDate(RETURNED_DATE)
            .rentalDate(RENTAL_DATE)
            .paidDays(RENTAL_DAYS)
            .surchargeAmountInCents(TOTAL_AMOUNT_IN_CENTS)
            .build();

    static final ReturnResponse RETURN_RESPONSE = ReturnResponse.builder()
            .totalSurchargeAmountInCents(TOTAL_AMOUNT_IN_CENTS)
            .returns(Collections.singletonList(RETURN_RESULT))
            .build();

}
