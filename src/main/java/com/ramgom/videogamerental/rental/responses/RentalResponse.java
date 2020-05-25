package com.ramgom.videogamerental.rental.responses;

import com.ramgom.videogamerental.rental.RentalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponse {
    private String userName;
    private List<RentalResult> rentals;
    private Long totalAmountInCents;
    private Integer totalLoyaltyPoints;

    @Value
    @Builder
    public static class RentalResult {
        Long id;
        String gameName;
        LocalDate rentalDate;
        LocalDate returnDate;
        Long amountInCents;
        Integer loyaltyPoints;

        public static RentalResult toRentalResult(RentalEntity rentalEntity, Long rentalPriceInCents) {
            return RentalResponse.RentalResult.builder()
                    .id(rentalEntity.getId())
                    .gameName(rentalEntity.getGame().getName())
                    .amountInCents(rentalPriceInCents)
                    .rentalDate(rentalEntity.getRentalDate())
                    .returnDate(rentalEntity.getRentalDate().plusDays(rentalEntity.getDays()))
                    .loyaltyPoints(rentalEntity.getLoyaltyPoints())
                    .build();
        }
    }
}
