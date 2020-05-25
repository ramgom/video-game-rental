package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.rental.RentalEntity;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCalculatorServiceTest {

    private static final GameType GAME_TYPE = GameType.CLASSIC;
    private static final Long AMOUNT_IN_CENTS = 500L;
    private static final Integer MIN_DAYS = 3;
    private static final Integer DAYS = 10;
    private static final Long PRICE = 1000L;
    private static final LocalDate RENTAL_DATE = LocalDate.now();
    private static final LocalDate RETURNED_DATE = RENTAL_DATE.plusDays(DAYS);
    private static final Integer PAID_DAYS = 5;
    private static final PriceEntity PRICE_ENTITY = PriceEntity.builder()
            .amountInCents(AMOUNT_IN_CENTS)
            .gameType(GAME_TYPE)
            .minimumDays(MIN_DAYS)
            .build();

    @Tested
    private PriceCalculatorService priceCalculatorService;

    @Injectable
    private PriceService priceService;

    @Injectable
    private PriceCalculator priceCalculator;

    @Test
    public void calculatePrice() {

        new Expectations() {{
            priceService.getPrice(GAME_TYPE); result = PRICE_ENTITY;

            priceCalculator.calculate(AMOUNT_IN_CENTS, MIN_DAYS, DAYS); result = PRICE;
        }};

        Long result = priceCalculatorService.calculatePrice(GAME_TYPE, DAYS);

        assertThat(result).isEqualTo(PRICE);

        new FullVerifications() {{}};
    }

    @Test
    public void calculateSurcharge(@Mocked RentalEntity rentalEntity) {

        new Expectations() {{
            rentalEntity.getRentalDate(); result = RENTAL_DATE;
            rentalEntity.getReturnedDate(); result = RETURNED_DATE;
            rentalEntity.getGame().getType(); result = GAME_TYPE;
            priceService.getPrice(GAME_TYPE); result = PRICE_ENTITY;

            rentalEntity.getDays(); result = PAID_DAYS;

            priceCalculator.surcharge(AMOUNT_IN_CENTS, PAID_DAYS, DAYS); result = PRICE;
        }};

        Long result = priceCalculatorService.calculateSurcharge(rentalEntity);

        assertThat(result).isEqualTo(PRICE);

        new FullVerifications() {{}};
    }
}