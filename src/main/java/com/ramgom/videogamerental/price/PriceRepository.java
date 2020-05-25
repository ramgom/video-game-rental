package com.ramgom.videogamerental.price;

import com.ramgom.videogamerental.game.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<PriceEntity, GameType> {
}
