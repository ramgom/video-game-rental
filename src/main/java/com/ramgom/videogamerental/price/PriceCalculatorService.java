package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import com.ramgom.videogamerental.rental.RentalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PriceCalculatorService {
    private final PriceService priceService;
    private final PriceCalculator priceCalculator;

    public Long calculatePrice(GameType gameType, Integer days) {

        PriceEntity priceEntity = priceService.getPrice(gameType);

        return priceCalculator.calculate(priceEntity.getAmountInCents(), priceEntity.getMinimumDays(), days);
    }

    public Long calculateSurcharge(RentalEntity rentalEntity) {
        long rentedDays = ChronoUnit.DAYS.between(rentalEntity.getRentalDate(), rentalEntity.getReturnedDate());
        PriceEntity priceEntity = priceService.getPrice(rentalEntity.getGame().getType());

        return priceCalculator.surcharge(priceEntity.getAmountInCents(), rentalEntity.getDays(), (int) rentedDays);
    }
}
