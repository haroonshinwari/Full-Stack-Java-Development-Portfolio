package guessthenumber.data;

import guessthenumber.TestApplicationConfiguration;
import guessthenumber.models.Game;
import guessthenumber.models.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDBTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;

    @Before
    public void setUp() {

        List<Game> games = gameDao.getAllGames();
        for(Game game : games) {
            gameDao.deleteGameByID(game.getGameID());
        }

        List<Round> rounds = roundDao.getAllRounds();
        for(Round round : rounds) {
            roundDao.deleteRoundsByGameID(round.getGameID());
        }

    }

    @Test
    public void testAddGetGame() {
        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);

        game = gameDao.addGame(game);

        Game fromDao = gameDao.getGameByID(game.getGameID());

        assertEquals(game, fromDao);
    }

    @Test
    public void getAllGames() {
        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);
        gameDao.addGame(game);

        Game game2 = new Game();
        game2.setRandomNumber("4567");
        game2.setFinished(false);
        gameDao.addGame(game2);

        List<Game> games = gameDao.getAllGames();

        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }

    @Test
    public void updateGameFinished() {
        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);

        game = gameDao.addGame(game);

        Game fromDao = gameDao.getGameByID(game.getGameID());

        assertEquals(game, fromDao);

        game.setFinished(true);

        assertNotEquals(game, fromDao);

        gameDao.updateGameFinished(game.getGameID());
        assertNotEquals(game, fromDao);

        fromDao = gameDao.getGameByID(game.getGameID());

        assertEquals(game, fromDao);
    }

    @Test
    public void deleteGameByID() {

        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);

        game = gameDao.addGame(game);

        Game fromDao = gameDao.getGameByID(game.getGameID());

        assertEquals(game, fromDao);

        gameDao.deleteGameByID(game.getGameID());

        fromDao = gameDao.getGameByID(game.getGameID());

        assertNull(fromDao);

    }
}