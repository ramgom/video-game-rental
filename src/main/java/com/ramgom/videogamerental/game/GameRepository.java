package com.ramgom.videogamerental.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface GameRepository extends JpaRepository<GameEntity, Long> {

    Optional<GameEntity> findByName(String name);
}
