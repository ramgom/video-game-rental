package com.ramgom.videogamerental.rental;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoyaltyPointsServiceTest {

    private static final Integer LOYALTY_POINTS = 10;
    private static final Long USER_ID = 123L;

    @Tested
    private LoyaltyPointsService loyaltyPointsService;

    @Injectable
    private RentalRepository rentalRepository;

    @Test
    public void getLoyaltyPoints() {
        new Expectations() {{
            rentalRepository.calculateLoyaltyPointsByUserId(USER_ID); result = LOYALTY_POINTS;
        }};

        Integer result = loyaltyPointsService.getLoyaltyPoints(USER_ID);

        assertThat(result).isEqualTo(LOYALTY_POINTS);

        new FullVerifications() {{}};
    }

    @Test
    public void getLoyaltyPoints_With_No_Points() {
        new Expectations() {{
            rentalRepository.calculateLoyaltyPointsByUserId(USER_ID); result = null;
        }};

        Integer result = loyaltyPointsService.getLoyaltyPoints(USER_ID);

        assertThat(result).isEqualTo(0);

        new FullVerifications() {{}};
    }
}