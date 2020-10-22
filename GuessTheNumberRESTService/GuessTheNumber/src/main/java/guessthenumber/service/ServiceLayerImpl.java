package guessthenumber.service;

import guessthenumber.data.GameDao;
import guessthenumber.data.RoundDao;
import guessthenumber.models.Game;
import guessthenumber.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServiceLayerImpl implements ServiceLayer{

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;

    //Through methods for accessing the Dao
    @Override
    public Game addGame(Game game) {
        return gameDao.addGame(game);
    }
    @Override
    public Game getGameByID(int id) {
        return gameDao.getGameByID(id);
    }
    @Override
    public List<Game> getAllGames() {
        return gameDao.getAllGames();
    }
    @Override
    public List<Round> getRoundsByGameID(int id) {
        return roundDao.getRoundsByGameID(id);
    }
    @Override
    public void addRound(Round round) {
        roundDao.addRound(round);
    }
    @Override
    public void updateGameFinished(int gameID) {
        gameDao.updateGameFinished(gameID);

    }

    //Business Logic - creating random number and checking guess against the random number
    public List<Integer> getRandomDigit() {
        Random random = new Random();
        List<Integer> luckyNumbersList = new ArrayList<>();

        int newDigit = random.nextInt(9);

        for(int i =1; i <= 4; i++) {

            while (luckyNumbersList.contains(newDigit)) {
                newDigit = random.nextInt(9);
            }
            luckyNumbersList.add(newDigit);
        }
        return luckyNumbersList;
    }

    public String checkGuess(String randomNumber, String guess) {
        int exactMatches = 0;
        int partialMatches = 0;

        String[] guessArray = guess.split("");
        String[] randomNumberArray = randomNumber.split("");

        //double for loop to compare the two numbers individually and determine exact or partial match
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (guessArray[i].equals(randomNumberArray[j])) {
                    if (i == j) {
                        exactMatches++;
                    }else {
                        partialMatches++;
                    }
                }
            }
        }

        String matches = "e:"+ exactMatches + ":p:" + partialMatches;
        return matches;
    }
}
