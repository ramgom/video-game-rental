package com.ramgom.videogamerental.price;

import mockit.Tested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCalculatorTest {

    private static final Long PRICE = 100L;
    private static final Integer INITIAL_DAYS = 5;
    private static final Integer MIN_DAYS = 3;

    @Tested
    private PriceCalculator priceCalculator;

    @ParameterizedTest
    @MethodSource("surchargeParams")
    public void surcharge(Long price, Integer payedDays, Integer actualDays, Long expectedResult) {
        Long result = priceCalculator.surcharge(price, payedDays, actualDays);

        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> surchargeParams() {
        return Stream.of(
                Arguments.arguments(PRICE, INITIAL_DAYS, INITIAL_DAYS + 5, PRICE * 5),
                Arguments.arguments(PRICE, INITIAL_DAYS, INITIAL_DAYS, 0L),
                Arguments.arguments(PRICE, INITIAL_DAYS, INITIAL_DAYS - 2, 0L)
        );
    }

    @ParameterizedTest
    @MethodSource("calculateParams")
    public void calculator(Long price, Integer minDays, Integer totalDays, Long expectedResult) {
        Long result = priceCalculator.calculate(price, minDays, totalDays);

        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> calculateParams() {
        return Stream.of(
                Arguments.arguments(PRICE, MIN_DAYS, INITIAL_DAYS, PRICE * (INITIAL_DAYS - MIN_DAYS + 1)),
                Arguments.arguments(PRICE, MIN_DAYS, MIN_DAYS, PRICE)
        );
    }
}