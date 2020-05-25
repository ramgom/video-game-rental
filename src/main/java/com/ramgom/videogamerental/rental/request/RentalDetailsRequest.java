package com.ramgom.videogamerental.rental.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class RentalDetailsRequest {

    @NotNull
    private Long gameId;

    @NotNull
    @Min(value = 1, message = "days value should be higher than 0")
    private Integer days;

    private LocalDate date;
}
