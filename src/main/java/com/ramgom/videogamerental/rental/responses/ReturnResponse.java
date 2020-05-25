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
public class ReturnResponse {

    List<ReturnResponse.ReturnResult> returns;
    Long totalSurchargeAmountInCents;

    @Value
    @Builder
    public static class ReturnResult {
        Long id;
        String gameName;
        LocalDate rentalDate;
        LocalDate returnedDate;
        Integer paidDays;
        Long surchargeAmountInCents;

        public static ReturnResult toReturnResult(RentalEntity rentalEntity, Long amountToPayInCents) {
            return ReturnResponse.ReturnResult.builder()
                    .id(rentalEntity.getId())
                    .gameName(rentalEntity.getGame().getName())
                    .paidDays(rentalEntity.getDays())
                    .rentalDate(rentalEntity.getRentalDate())
                    .returnedDate(rentalEntity.getReturnedDate())
                    .surchargeAmountInCents(amountToPayInCents)
                    .build();
        }
    }


}
