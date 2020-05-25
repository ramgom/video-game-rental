package com.ramgom.videogamerental.game;

import com.ramgom.videogamerental.game.exceptions.DuplicateGameException;
import com.ramgom.videogamerental.game.requests.CreateGameRequest;
import com.ramgom.videogamerental.game.responses.GameResponse;
import com.ramgom.videogamerental.rental.exceptions.GameNotFoundException;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class GameServiceTest {

    private static final String GAME_NAME = "name";
    private static final GameType GAME_TYPE = GameType.CLASSIC;
    private static final Long GAME_ID = 123L;

    @Tested
    private GameService gameService;

    @Injectable
    private GameRepository gameRepository;

    @Test
    public void getGames(
            @Mocked GameEntity gameEntity1,
            @Mocked GameEntity gameEntity2,
            @Mocked GameResponse gameResponse1,
            @Mocked GameResponse gameResponse2
            ) {
        new Expectations() {{
            gameRepository.findAll(); result = Arrays.asList(gameEntity1, gameEntity2);
            GameResponse.toGameResponse(gameEntity1); result = gameResponse1;
            GameResponse.toGameResponse(gameEntity2); result = gameResponse2;
        }};

        List<GameResponse> games = gameService.getGames();

        assertThat(games).containsOnly(gameResponse1, gameResponse2);
    }

    @Test
    public void getGame_With_Valid_ID(@Mocked GameEntity gameEntity) {
        new Expectations() {{
            gameRepository.findById(GAME_ID); result = Optional.of(gameEntity);
        }};

        GameEntity result = gameService.getGame(GAME_ID);

        assertThat(result).isSameAs(gameEntity);

        new FullVerifications() {{}};
    }

    @Test
    public void getGame_With_Invalid_ID() {
        new Expectations() {{
            gameRepository.findById(GAME_ID); result = Optional.empty();
        }};

        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> gameService.getGame(GAME_ID));

        new FullVerifications() {{}};
    }

    @Test
    public void addGame(@Mocked CreateGameRequest request, @Mocked GameResponse gameResponse) {

        GameEntity gameEntity = GameEntity.builder().build();

        new Expectations() {{
            request.getName(); result = GAME_NAME;
            request.getType(); result = GAME_TYPE;

            gameRepository.save((GameEntity) any); result = gameEntity;
            GameResponse.toGameResponse(gameEntity); result = gameResponse;
        }};

        GameResponse response = gameService.addGame(request);

        assertThat(response).isSameAs(gameResponse);

        new FullVerifications() {{
            GameEntity capturedGameEntity;
            gameRepository.save(capturedGameEntity = withCapture());

            assertThat(capturedGameEntity.getName()).isEqualTo(GAME_NAME);
            assertThat(capturedGameEntity.getType()).isEqualTo(GAME_TYPE);
        }};
    }

    @Test
    public void addGame_With_Duplicate(@Mocked CreateGameRequest request) {

        GameEntity gameEntity = GameEntity.builder().build();

        new Expectations() {{
            request.getName(); result = GAME_NAME;
            request.getType(); result = GAME_TYPE;

            gameRepository.save((GameEntity) any); result = new DataIntegrityViolationException("Error");
            gameRepository.findByName(GAME_NAME); result = Optional.of(gameEntity);
        }};

        assertThatExceptionOfType(DuplicateGameException.class)
                .isThrownBy(() -> gameService.addGame(request))
                .satisfies(ex -> assertThat(ex.getGame()).isSameAs(gameEntity));

        new FullVerifications() {{}};
    }
}