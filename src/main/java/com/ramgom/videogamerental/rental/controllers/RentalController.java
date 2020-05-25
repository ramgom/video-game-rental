package com.ramgom.videogamerental.rental.controllers;

import com.ramgom.videogamerental.rental.request.ReturnDetailsRequest;
import com.ramgom.videogamerental.rental.responses.ReturnResponse;
import com.ramgom.videogamerental.rental.RentalService;
import com.ramgom.videogamerental.rental.request.RentalDetailsRequest;
import com.ramgom.videogamerental.rental.responses.RentalResponse;
import com.ramgom.videogamerental.rental.responses.UserRentalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@Slf4j
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/users/{user_id}")
    public RentalResponse rental(
            @PathVariable("user_id") Long userId,
            @RequestBody @Valid List<RentalDetailsRequest> rentals) {
        log.info("operation=rental, userId={}, request={}", userId, rentals);
        return rentalService.rentGame(userId, rentals);
    }

    @GetMapping("/users/{user_id}")
    public UserRentalResponse getRentals(
            @PathVariable("user_id") Long userId,
            @RequestParam(name = "historical", defaultValue = "false") Boolean historical) {
        log.info("operation=getRentals, userId={}, historical={}", userId, historical);
        return rentalService.getRentals(userId, historical);
    }

    @DeleteMapping
    public ReturnResponse rentalReturn(@RequestBody @Valid List<ReturnDetailsRequest> returns) {
        log.info("operation=rentalReturn, request={}", returns);
        return rentalService.rentalReturns(returns);
    }
}
