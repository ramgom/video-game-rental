package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.price.exceptions.PriceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    @Cacheable("prices")
    public PriceEntity getPrice(GameType gameType) {
        return priceRepository.findById(gameType)
                .orElseThrow(() -> new PriceNotFoundException(gameType));
    }
}
