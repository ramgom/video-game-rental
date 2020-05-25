package com.ramgom.videogamerental.rental;

import com.ramgom.videogamerental.users.UserEntity;
import com.ramgom.videogamerental.game.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    @Query("select r from RentalEntity r where r.game = :gameEntity and r.returned = false")
    Optional<RentalEntity> findNotReturnedByGame(GameEntity gameEntity);

    @Query("select sum(r.loyaltyPoints) from RentalEntity r where r.user.id = :userId")
    Integer calculateLoyaltyPointsByUserId(Long userId);

    @Query("select r from RentalEntity r where r.user = :user and r.returned = false")
    List<RentalEntity> findAllActiveRentalsByUser(UserEntity user);

    @Query("select r from RentalEntity r where r.user = :user")
    List<RentalEntity> findAllRentalsByUser(UserEntity user);
}
