package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.price.exceptions.PriceNotFoundException;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PriceServiceTest {

    private static final GameType GAME_TYPE = GameType.STANDARD;

    @Tested
    private PriceService priceService;

    @Injectable
    private PriceRepository priceRepository;

    @Test
    public void getPrice(@Mocked PriceEntity priceEntity) {

        new Expectations() {{
            priceRepository.findById(GAME_TYPE); result = Optional.of(priceEntity);
        }};

        PriceEntity result = priceService.getPrice(GAME_TYPE);

        assertThat(result).isSameAs(priceEntity);

        new FullVerifications() {{}};
    }

    @Test
    public void getPrice_With_Price_Not_Found() {
        new Expectations() {{
            priceRepository.findById(GAME_TYPE); result = Optional.empty();
        }};

        assertThatExceptionOfType(PriceNotFoundException.class)
                .isThrownBy(() -> priceService.getPrice(GAME_TYPE))
                .satisfies(ex -> assertThat(ex.getGameType()).isEqualTo(GAME_TYPE));
    }
}