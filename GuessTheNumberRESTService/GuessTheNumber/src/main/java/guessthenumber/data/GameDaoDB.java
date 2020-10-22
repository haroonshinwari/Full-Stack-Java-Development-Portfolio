package guessthenumber.data;

import guessthenumber.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GameDaoDB implements GameDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Game addGame(Game game) {
        final String INSERT_GAME = "INSERT INTO game(RandomNumber, finished) VALUES(?,?)";
        jdbc.update(INSERT_GAME, game.getRandomNumber(), game.isFinished()); //returns the number of rows affected

        int newGameID = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameID(newGameID);
        return game;
    }

    @Override
    public Game getGameByID(int id) {
        try {
            final String SELECT_GAME_BY_ID = "SELECT * FROM game WHERE GameID = ?";
            return jdbc.queryForObject(SELECT_GAME_BY_ID, new GameMapper(), id);
        }catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Game> getAllGames() {
        final String SELECT_ALL_GAMES = "SELECT * FROM game";
        return jdbc.query(SELECT_ALL_GAMES, new GameMapper());
    }


    @Override
    public void updateGameFinished(int gameID) {
        final String UPDATE_GAME_FINISHED = "UPDATE game SET Finished = 1 WHERE GameID = ?";
        jdbc.update(UPDATE_GAME_FINISHED, gameID);
    }

    @Override
    @Transactional
    public void deleteGameByID(int gameID) {
        final String DELETE_ROUNDS_BY_GAMEID = "DELETE FROM gamerounds WHERE GameID = ?";
        jdbc.update(DELETE_ROUNDS_BY_GAMEID , gameID);

        final String DELETE_GAME_BY_GAMEID = "DELETE FROM game WHERE GameID = ? ";
        jdbc.update(DELETE_GAME_BY_GAMEID, gameID);
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameID(rs.getInt("GameID"));
            game.setRandomNumber(rs.getString("RandomNumber"));
            game.setFinished(rs.getBoolean("Finished"));
            return game;
        }
    }
}
