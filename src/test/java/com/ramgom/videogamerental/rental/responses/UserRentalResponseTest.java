package com.ramgom.videogamerental.rental.responses;

import com.ramgom.videogamerental.rental.RentalEntity;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserRentalResponseTest {

    private static final Long RENTAL_ID = 123L;
    private static final String GAME_NAME = "name";
    private static final Integer RENTAL_DAYS = 5;
    private static final LocalDate RENTAL_DATE = LocalDate.now();
    private static final LocalDate INITIAL_RETURN_DATE = RENTAL_DATE.plusDays(RENTAL_DAYS);
    private static final LocalDate RETURNED_DATE = RENTAL_DATE.plusDays(RENTAL_DAYS + 1);
    private static final Integer LOYALTY_POINTS = 10;

    @Test
    public void toRentalDetails(@Mocked RentalEntity rental) {

        new Expectations() {{
            rental.getId(); result = RENTAL_ID;
            rental.getGame().getName(); result = GAME_NAME;
            rental.getRentalDate(); result = RENTAL_DATE;
            rental.getReturnedDate(); result = RETURNED_DATE;
            rental.getDays(); result = RENTAL_DAYS;
        }};

        UserRentalResponse.RentalDetails result = UserRentalResponse.RentalDetails.toRentalDetails(rental);

        assertThat(result.getId()).isEqualTo(RENTAL_ID);
        assertThat(result.getGameName()).isEqualTo(GAME_NAME);
        assertThat(result.getRentalDate()).isEqualTo(RENTAL_DATE);
        assertThat(result.getInitialReturnDate()).isEqualTo(INITIAL_RETURN_DATE);
        assertThat(result.getReturnedDate()).isEqualTo(RETURNED_DATE);

        new FullVerifications() {{}};
    }
}