package com.ramgom.videogamerental.rental;

import com.ramgom.videogamerental.users.UserEntity;
import com.ramgom.videogamerental.game.GameEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RENTALS")
@Data
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="GAME_ID", nullable=false)
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private UserEntity user;

    @Column(name = "RENTAL_DAYS")
    private Integer days;

    @Column(name = "RENTAL_DATE")
    private LocalDate rentalDate;

    @Column(name = "RETURNED_DATE")
    private LocalDate returnedDate;

    @Column(name = "RETURNED")
    @Builder.Default private Boolean returned = Boolean.FALSE;

    @Column(name = "LOYALTY_POINTS", nullable = false)
    private Integer loyaltyPoints;
}
