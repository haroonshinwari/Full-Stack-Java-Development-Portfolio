package guessthenumber.service;

import guessthenumber.models.Game;
import guessthenumber.models.Round;

import java.util.List;

public interface ServiceLayer {

    //through methods already tested in the DAO
    Game addGame(Game game);
    Game getGameByID(int id);
    List<Game> getAllGames();
    void updateGameFinished(int gameID);

    List<Round> getRoundsByGameID(int id);
    void addRound(Round round);



    /**
     * This method creates the 4 random digits required for our lucky number satisfying the condition that they are not the same
     * @return List of 4 Integers representing the lucky number
     */
    List<Integer> getRandomDigit();

    /**
     *
     * @param randomNumber
     * @param guess
     * @return
     */
    String checkGuess(String randomNumber, String guess);

}
