package com.ramgom.videogamerental.game;

import com.ramgom.videogamerental.game.exceptions.DuplicateGameException;
import com.ramgom.videogamerental.game.requests.CreateGameRequest;
import com.ramgom.videogamerental.game.responses.GameResponse;
import com.ramgom.videogamerental.rental.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public List<GameResponse> getGames() {
        return gameRepository.findAll().stream().map(GameResponse::toGameResponse).collect(Collectors.toList());
    }

    public GameResponse addGame(CreateGameRequest request) {

        GameEntity game = GameEntity.builder().name(request.getName()).type(request.getType()).build();

        try {
            return GameResponse.toGameResponse(gameRepository.save(game));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateGameException(gameRepository.findByName(game.getName()).get());
        }
    }

    public GameEntity getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(GameNotFoundException::new);
    }
}
