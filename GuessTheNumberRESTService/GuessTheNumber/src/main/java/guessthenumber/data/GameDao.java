package guessthenumber.data;

import guessthenumber.models.Game;

import java.util.List;

public interface GameDao {

    Game addGame(Game game);
    Game getGameByID(int id);
    List<Game> getAllGames();
    void updateGameFinished(int gameID);
    void deleteGameByID(int gameID);
}
