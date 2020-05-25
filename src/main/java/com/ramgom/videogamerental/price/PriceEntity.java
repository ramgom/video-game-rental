package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "PRICES")
@AllArgsConstructor
@NoArgsConstructor
public class PriceEntity {

    @Id
    @Column(name = "GAME_TYPE")
    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @Column(name = "MIN_DAYS")
    private Integer minimumDays;

    @Column(name = "AMOUNT_IN_CENTS")
    private Long amountInCents;

    @Column(name = "LOYALTY_POINTS")
    private Integer loyaltyPoints;
}
