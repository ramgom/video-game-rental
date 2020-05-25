package com.ramgom.videogamerental.rental;

import com.ramgom.videogamerental.price.PriceEntity;
import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.users.UserEntity;

import java.time.LocalDate;

public class RentalServiceTestFixture {

    static final Long USER_ID = 123L;
    static final String USER_NAME = "user";
    static final UserEntity USER = UserEntity.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .build();

    static final Long GAME_ID1 = 456L;
    static final String GAME_NAME1 = "game1";
    static final GameType GAME_TYPE1 = GameType.NEW_RELEASE;
    static final GameEntity GAME1 = GameEntity.builder()
            .id(GAME_ID1)
            .name(GAME_NAME1)
            .type(GAME_TYPE1)
            .build();

    static final Long GAME_ID2= 789L;
    static final String GAME_NAME2 = "game2";
    static final GameType GAME_TYPE2 = GameType.STANDARD;
    static final GameEntity GAME2 = GameEntity.builder()
            .id(GAME_ID2)
            .name(GAME_NAME2)
            .type(GAME_TYPE2)
            .build();


    static final Integer LOYALTY_POINTS1 = 10;
    static final PriceEntity PRICE1 = PriceEntity.builder().loyaltyPoints(LOYALTY_POINTS1).build();

    static final Integer LOYALTY_POINTS2 = 50;
    static final PriceEntity PRICE2 = PriceEntity.builder().loyaltyPoints(LOYALTY_POINTS2).build();

    static final Integer TOTAL_LOYALTY_POINTS = LOYALTY_POINTS1 + LOYALTY_POINTS2;

    static final Long RENTAL_PRICE1 = 400L;
    static final Long RENTAL_PRICE2 = 200L;

    static final Long TOTAL_RENTAL_PRICE = RENTAL_PRICE1 + RENTAL_PRICE2;

    static final Integer RENTAL_DAYS = 5;

    static final LocalDate REQUEST_DATE = LocalDate.now().plusDays(10);


}
