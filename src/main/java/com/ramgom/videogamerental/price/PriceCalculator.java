package com.ramgom.videogamerental.price;

import org.springframework.stereotype.Service;

@Service
public class PriceCalculator {

    public Long calculate(Long price, Integer minDays, Integer totalDays) {

        Long initialPrice = minDays == 0 ? 0:price;
        int remainingDays = totalDays - minDays;
        Long remainingPrice = remainingDays < 0 ? 0:remainingDays * price;

        return initialPrice + remainingPrice;
    }

    public Long surcharge(Long price, Integer paidDays, Integer actualDays) {
        Integer rentedDays = actualDays - paidDays;
        return rentedDays < 0 ? 0:price * rentedDays;
    }
}
