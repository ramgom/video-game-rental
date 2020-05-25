package com.ramgom.videogamerental.rental.responses;

import com.ramgom.videogamerental.rental.RentalEntity;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnResponseTest {

    private static final Long RENTAL_ID = 123L;
    private static final String GAME_NAME = "name";
    private static final Long SURCHARGE_AMOUNT_IN_CENTS = 500L;
    private static final Integer RENTAL_DAYS = 10;
    private static final LocalDate RENTAL_DATE = LocalDate.now();
    private static final LocalDate RETURN_DATE = RENTAL_DATE.plusDays(RENTAL_DAYS);
    private static final Integer LOYALTY_POINTS = 10;

    @Test
    public void toReturnResult(@Mocked RentalEntity rental) {
        new Expectations() {{
            rental.getId(); result = RENTAL_ID;
            rental.getGame().getName(); result = GAME_NAME;
            rental.getDays(); result = RENTAL_DAYS;
            rental.getRentalDate(); result = RENTAL_DATE;
            rental.getReturnedDate(); result = RETURN_DATE;
        }};

        ReturnResponse.ReturnResult result = ReturnResponse.ReturnResult.toReturnResult(rental, SURCHARGE_AMOUNT_IN_CENTS);

        assertThat(result.getId()).isEqualTo(RENTAL_ID);
        assertThat(result.getGameName()).isEqualTo(GAME_NAME);
        assertThat(result.getSurchargeAmountInCents()).isEqualTo(SURCHARGE_AMOUNT_IN_CENTS);
        assertThat(result.getRentalDate()).isEqualTo(RENTAL_DATE);
        assertThat(result.getReturnedDate()).isEqualTo(RETURN_DATE);
        assertThat(result.getPaidDays()).isEqualTo(RENTAL_DAYS);

        new FullVerifications() {{}};
    }
}