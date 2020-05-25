package com.ramgom.videogamerental.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoyaltyPointsService {

    private final RentalRepository rentalRepository;

    public Integer getLoyaltyPoints(Long user_id) {
        return Optional.ofNullable(rentalRepository.calculateLoyaltyPointsByUserId(user_id))
                .orElse(0);
    }
}
