package com.ramgom.videogamerental.rental.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class ReturnDetailsRequest {

    @NotNull
    private Long gameId;

    private LocalDate date;
}
