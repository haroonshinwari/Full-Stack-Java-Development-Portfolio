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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoDBTest {

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
    public void testAddGetRound() {
        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);
        gameDao.addGame(game);

        Round round = new Round();
        round.setGameID(game.getGameID());
        round.setRoundID(1);
        round.setGuess("4567");
        round.setResult("e:0:p:0");
        round.setTimeOfRound(LocalDateTime.now().withNano(0));

        List<Round> listOfRounds = new ArrayList<>();
        listOfRounds.add(round);

        roundDao.addRound(round);

        List<Round> fromDao = roundDao.getRoundsByGameID(game.getGameID());

        assertEquals(listOfRounds, fromDao);
    }



    @Test
    public void getAllRounds() {
        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);
        gameDao.addGame(game);

        Game game2 = new Game();
        game2.setRandomNumber("1587");
        game2.setFinished(false);
        gameDao.addGame(game2);

        Round round = new Round();
        round.setGameID(game.getGameID());
        round.setRoundID(1);
        round.setGuess("4567");
        round.setResult("e:0:p:0");
        round.setTimeOfRound(LocalDateTime.now().withNano(0));

        Round round2 = new Round();
        round2.setGameID(game2.getGameID());
        round2.setRoundID(1);
        round2.setGuess("4567");
        round2.setResult("e:1:p:1");
        round2.setTimeOfRound(LocalDateTime.now().withNano(0));

        Round round3 = new Round();
        round3.setGameID(game2.getGameID());
        round3.setRoundID(2);
        round3.setGuess("1689");
        round3.setResult("e:0:p:1");
        round3.setTimeOfRound(LocalDateTime.now().withNano(0));



        List<Round> listOfRounds = new ArrayList<>();
        listOfRounds.add(round);
        listOfRounds.add(round2);
        listOfRounds.add(round3);


        roundDao.addRound(round);
        roundDao.addRound(round2);
        roundDao.addRound(round3);

        List<Round> fromDao = roundDao.getAllRounds();

        assertEquals(3, fromDao.size());
        assertTrue(fromDao.contains(round));
        assertTrue(fromDao.contains(round2));
        assertTrue(fromDao.contains(round3));
    }

    @Test
    public void deleteRoundsByGameID() {

        Game game = new Game();
        game.setRandomNumber("1234");
        game.setFinished(false);
        gameDao.addGame(game);

        Round round = new Round();
        round.setGameID(game.getGameID());
        round.setRoundID(1);
        round.setGuess("4567");
        round.setResult("e:0:p:0");
        round.setTimeOfRound(LocalDateTime.now().withNano(0));

        roundDao.addRound(round);

        roundDao.deleteRoundsByGameID(game.getGameID());

        List<Round> fromDao = roundDao.getRoundsByGameID(game.getGameID());

        assertEquals(0, fromDao.size());
        assertTrue(fromDao.isEmpty());

    }
}