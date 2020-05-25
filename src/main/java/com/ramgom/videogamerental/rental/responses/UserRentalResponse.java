package com.ramgom.videogamerental.rental.responses;

import com.ramgom.videogamerental.rental.RentalEntity;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class UserRentalResponse {

    String userName;
    List<RentalDetails> rentals;

    @Value
    @Builder
    public static class RentalDetails {
        Long id;
        String gameName;
        LocalDate rentalDate;
        LocalDate initialReturnDate;
        LocalDate returnedDate;

        public static RentalDetails toRentalDetails(RentalEntity rentalEntity) {
            return UserRentalResponse.RentalDetails.builder()
                    .id(rentalEntity.getId())
                    .gameName(rentalEntity.getGame().getName())
                    .rentalDate(rentalEntity.getRentalDate())
                    .initialReturnDate(rentalEntity.getRentalDate().plusDays(rentalEntity.getDays()))
                    .returnedDate(rentalEntity.getReturnedDate())
                    .build();
        }
    }
}
