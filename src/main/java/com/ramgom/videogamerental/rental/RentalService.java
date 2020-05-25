package com.ramgom.videogamerental.rental;

import com.ramgom.videogamerental.price.PriceCalculatorService;
import com.ramgom.videogamerental.price.PriceEntity;
import com.ramgom.videogamerental.price.PriceService;
import com.ramgom.videogamerental.rental.exceptions.GameNotRentedException;
import com.ramgom.videogamerental.rental.exceptions.GameRentedException;
import com.ramgom.videogamerental.rental.request.RentalDetailsRequest;
import com.ramgom.videogamerental.rental.request.ReturnDetailsRequest;
import com.ramgom.videogamerental.rental.responses.ReturnResponse;
import com.ramgom.videogamerental.users.UserEntity;
import com.ramgom.videogamerental.users.UserService;
import com.ramgom.videogamerental.game.GameEntity;
import com.ramgom.videogamerental.game.GameService;
import com.ramgom.videogamerental.rental.responses.RentalResponse;
import com.ramgom.videogamerental.rental.responses.UserRentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final GameService gameService;
    private final PriceCalculatorService priceCalculatorService;
    private final UserService userService;
    private final PriceService priceService;

    private final static BiConsumer<RentalResponse, RentalResponse.RentalResult> RENTAL_COLLECTOR
            = (rentalResponse, rentalResult) -> {
        rentalResponse.getRentals().add(rentalResult);
        rentalResponse.setTotalAmountInCents(rentalResponse.getTotalAmountInCents() + rentalResult.getAmountInCents());
        rentalResponse.setTotalLoyaltyPoints(rentalResponse.getTotalLoyaltyPoints() + rentalResult.getLoyaltyPoints());
    };

    private final static BiConsumer<ReturnResponse, ReturnResponse.ReturnResult> RETURN_COLLECTOR
            = (returnResponse, returnResult) -> {
        returnResponse.getReturns().add(returnResult);
        returnResponse.setTotalSurchargeAmountInCents(returnResponse.getTotalSurchargeAmountInCents() + returnResult.getSurchargeAmountInCents());
    };

    @Transactional
    public RentalResponse rentGame(Long userId, List<RentalDetailsRequest> rentals) {

        UserEntity user = userService.findUserById(userId);

        return rentals.stream()
                .map(rental -> rentGame(user, rental))
                .collect(
                        RentalResponse.builder().userName(user.getName()).totalAmountInCents(0L).totalLoyaltyPoints(0).rentals(new ArrayList<>())::build,
                        RENTAL_COLLECTOR,
                        (one, two) -> {}
                        );
    }

    @Transactional
    public ReturnResponse rentalReturns(List<ReturnDetailsRequest> returns) {
        return returns.stream()
                .map(this::rentalReturn)
                .collect(
                        ReturnResponse.builder().totalSurchargeAmountInCents(0L).returns(new ArrayList<>())::build,
                        RETURN_COLLECTOR,
                        (one, two) -> {}
                );
    }

    private RentalResponse.RentalResult rentGame(UserEntity userEntity, RentalDetailsRequest rental) {

        GameEntity game = gameService.getGame(rental.getGameId());
        Long rentalPrice = priceCalculatorService.calculatePrice(game.getType(), rental.getDays());
        PriceEntity price = priceService.getPrice(game.getType());

        RentalEntity rentalEntity = RentalEntity.builder()
                .game(game)
                .user(userEntity)
                .rentalDate(Optional.ofNullable(rental.getDate()).orElse(LocalDate.now()))
                .days(rental.getDays())
                .loyaltyPoints(price.getLoyaltyPoints())
                .build();

        if (rentalRepository.findNotReturnedByGame(game).isPresent()) {
            throw new GameRentedException(game.getName());
        }

        RentalEntity result = rentalRepository.save(rentalEntity);

        return RentalResponse.RentalResult.toRentalResult(result, rentalPrice);
    }

    private ReturnResponse.ReturnResult rentalReturn(ReturnDetailsRequest returnDetails) {
        GameEntity game = gameService.getGame(returnDetails.getGameId());

        return rentalRepository.findNotReturnedByGame(game)
                .map(rental -> {
                    rental.setReturnedDate(Optional.ofNullable(returnDetails.getDate()).orElse(LocalDate.now()));
                    rental.setReturned(true);

                    RentalEntity result = rentalRepository.save(rental);

                    Long amountToPay = priceCalculatorService.calculateSurcharge(result);

                    return ReturnResponse.ReturnResult.toReturnResult(result, amountToPay);
                }).orElseThrow(() -> new GameNotRentedException(game.getName()));
    }

    public UserRentalResponse getRentals(Long userId, Boolean historical) {
        UserEntity user = userService.findUserById(userId);
        List<RentalEntity> rentals = historical ? rentalRepository.findAllRentalsByUser(user) : rentalRepository.findAllActiveRentalsByUser(user);

        List<UserRentalResponse.RentalDetails> rentalDetails = rentals.stream()
                .map(UserRentalResponse.RentalDetails::toRentalDetails).collect(Collectors.toList());

        return UserRentalResponse.builder()
                .userName(user.getName())
                .rentals(rentalDetails)
                .build();
    }
}
