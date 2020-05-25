package com.ramgom.videogamerental.rental.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramgom.videogamerental.VideoGameRentalApplication;
import com.ramgom.videogamerental.price.exceptions.PriceNotFoundException;
import com.ramgom.videogamerental.rental.RentalService;
import com.ramgom.videogamerental.rental.exceptions.GameNotFoundException;
import com.ramgom.videogamerental.rental.exceptions.GameRentedException;
import com.ramgom.videogamerental.users.exceptions.UserNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VideoGameRentalApplication.class)
@AutoConfigureMockMvc
class RentalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RentalService rentalService;

    @Test
    public void rental() throws Exception {
        when(rentalService.rentGame(eq(RentalControllerTestFixtures.USER_ID), any(List.class))).thenReturn(RentalControllerTestFixtures.RENTAL_RESPONSE);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/rentals/users/" + RentalControllerTestFixtures.USER_ID)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(RentalControllerTestFixtures.RENTAL_DETAILS_REQUEST)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", Matchers.is(RentalControllerTestFixtures.USER_NAME)))
                .andExpect(jsonPath("$.totalAmountInCents").value(Matchers.is(RentalControllerTestFixtures.TOTAL_AMOUNT_IN_CENTS), Long.class))
                .andExpect(jsonPath("$.totalLoyaltyPoints", Matchers.is(RentalControllerTestFixtures.TOTAL_LOYALTY_POINTS)))
                .andExpect(jsonPath("$.rentals", hasSize(1)))
                .andExpect(jsonPath("$.rentals[0].id").value(Matchers.is(RentalControllerTestFixtures.RENTAL_ID), Long.class))
                .andExpect(jsonPath("$.rentals[0].gameName", Matchers.is(RentalControllerTestFixtures.GAME_NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentals[0].rentalDate", Matchers.is(RentalControllerTestFixtures.RENTAL_DATE.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentals[0].returnDate", Matchers.is(RentalControllerTestFixtures.RETURN_DATE.toString())))
                .andExpect(jsonPath("$.rentals[0].amountInCents").value(Matchers.is(RentalControllerTestFixtures.TOTAL_AMOUNT_IN_CENTS), Long.class))
                .andExpect(jsonPath("$.rentals[0].loyaltyPoints", Matchers.is(RentalControllerTestFixtures.TOTAL_LOYALTY_POINTS)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"gameId\": 123}", "{\"gameId\": 123, \"days\": -5}", "{\"days\": 5}"})
    public void rental_With_Invalid_Request(String request) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rentals/users/" + RentalControllerTestFixtures.USER_ID).content(request).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @ParameterizedTest
    @MethodSource("rental_With_Errors_Params")
    public void rental_With_Errors(RuntimeException exception, ResultMatcher statusResultMatcher) throws Exception {
        when(rentalService.rentGame(eq(RentalControllerTestFixtures.USER_ID), any(List.class))).thenThrow(exception);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/rentals/users/" + RentalControllerTestFixtures.USER_ID)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(RentalControllerTestFixtures.RENTAL_DETAILS_REQUEST)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(statusResultMatcher)
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    public static Stream<Arguments> rental_With_Errors_Params() {
        return Stream.of(
                Arguments.of(new UserNotFoundException(), status().isNotFound()),
                Arguments.of(new GameNotFoundException(), status().isBadRequest()),
                Arguments.of(new PriceNotFoundException(RentalControllerTestFixtures.GAME_TYPE), status().isBadRequest()),
                Arguments.of(new GameRentedException(RentalControllerTestFixtures.GAME_NAME), status().isBadRequest())
        );
    }

    @Test
    public final void getRentals() throws Exception {
        when(rentalService.getRentals(RentalControllerTestFixtures.USER_ID, Boolean.FALSE)).thenReturn(RentalControllerTestFixtures.USER_RENTAL_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/users/" + RentalControllerTestFixtures.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", Matchers.is(RentalControllerTestFixtures.USER_NAME)))
                .andExpect(jsonPath("$.rentals", hasSize(1)))
                .andExpect(jsonPath("$.rentals[0].id").value(Matchers.is(RentalControllerTestFixtures.RENTAL_ID), Long.class))
                .andExpect(jsonPath("$.rentals[0].gameName", Matchers.is(RentalControllerTestFixtures.GAME_NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentals[0].rentalDate", Matchers.is(RentalControllerTestFixtures.RENTAL_DATE.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentals[0].initialReturnDate", Matchers.is(RentalControllerTestFixtures.RETURN_DATE.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentals[0].returnedDate", Matchers.is(RentalControllerTestFixtures.RETURNED_DATE.toString())));

    }

    @Test
    public final void getRentals_With_Historical_Data() throws Exception {
        when(rentalService.getRentals(RentalControllerTestFixtures.USER_ID, Boolean.TRUE)).thenReturn(RentalControllerTestFixtures.USER_RENTAL_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/users/" + RentalControllerTestFixtures.USER_ID).param("historical", Boolean.TRUE.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public final void getRentals_With_User_Not_Found() throws Exception {
        when(rentalService.getRentals(RentalControllerTestFixtures.USER_ID, Boolean.TRUE)).thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/users/" + RentalControllerTestFixtures.USER_ID).param("historical", Boolean.TRUE.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void rentalReturn() throws Exception {
        when(rentalService.rentalReturns(any(List.class))).thenReturn(RentalControllerTestFixtures.RETURN_RESPONSE);

        mockMvc.perform(
                delete("/rentals")
                        .content(objectMapper.writeValueAsString(Collections.singletonList(RentalControllerTestFixtures.RETURN_DETAILS_REQUEST)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSurchargeAmountInCents").value(Matchers.is(RentalControllerTestFixtures.TOTAL_AMOUNT_IN_CENTS), Long.class))
                .andExpect(jsonPath("$.returns", hasSize(1)))
                .andExpect(jsonPath("$.returns[0].id").value(Matchers.is(RentalControllerTestFixtures.RENTAL_ID), Long.class))
                .andExpect(jsonPath("$.returns[0].gameName", Matchers.is(RentalControllerTestFixtures.GAME_NAME)))
                .andExpect(jsonPath("$.returns[0].paidDays", Matchers.is(RentalControllerTestFixtures.RENTAL_DAYS)))
                .andExpect(jsonPath("$.returns[0].surchargeAmountInCents").value(Matchers.is(RentalControllerTestFixtures.TOTAL_AMOUNT_IN_CENTS), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returns[0].rentalDate", Matchers.is(RentalControllerTestFixtures.RENTAL_DATE.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returns[0].returnedDate", Matchers.is(RentalControllerTestFixtures.RETURNED_DATE.toString())));
    }

    @ParameterizedTest
    @MethodSource("rentalReturn_With_Errors_Params")
    public void rentalReturn_With_Errors(RuntimeException exception, ResultMatcher statusResultMatcher) throws Exception {
        when(rentalService.rentalReturns(any(List.class))).thenThrow(exception);

        mockMvc.perform(
                        delete("/rentals")
                                .content(objectMapper.writeValueAsString(Collections.singletonList(RentalControllerTestFixtures.RETURN_DETAILS_REQUEST)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(statusResultMatcher)
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    public static Stream<Arguments> rentalReturn_With_Errors_Params() {
        return Stream.of(
                Arguments.of(new UserNotFoundException(), status().isNotFound()),
                Arguments.of(new GameNotFoundException(), status().isBadRequest()),
                Arguments.of(new GameRentedException(RentalControllerTestFixtures.GAME_NAME), status().isBadRequest())
        );
    }
}