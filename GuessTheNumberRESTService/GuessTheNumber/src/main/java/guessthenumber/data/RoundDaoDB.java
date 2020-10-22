package guessthenumber.data;

import guessthenumber.models.Round;
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
public class RoundDaoDB implements RoundDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public void addRound(Round round) {
        jdbc.update("INSERT INTO gamerounds(GameID,RoundID,Guess,Result,TimeOfRound) VALUES(?,?,?,?,?)",
                round.getGameID(),round.getRoundID(),round.getGuess(),round.getResult(),round.getTimeOfRound()); //returns the number of rows affected
    }

    @Override
    public List<Round> getRoundsByGameID(int id) {
        try {
            return jdbc.query("SELECT * FROM gamerounds WHERE GameID = ? ORDER BY TimeOfRound ASC ", new RoundMapper(), id);
        }catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Round> getAllRounds() {
        return jdbc.query("SELECT * FROM gamerounds", new RoundMapper());
    }

    @Override
    @Transactional
    public void deleteRoundsByGameID(int gameID) {
        final String DELETE_ROUNDS_BY_GAMEID = "DELETE FROM gamerounds WHERE GameID = ? ";
        jdbc.update(DELETE_ROUNDS_BY_GAMEID, gameID);
    }



    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();

            round.setGameID(rs.getInt("GameID"));
            round.setRoundID(rs.getInt("RoundID"));
            round.setGuess(rs.getString("Guess"));
            round.setResult(rs.getString("Result"));
            round.setTimeOfRound(rs.getTimestamp("TimeOfRound").toLocalDateTime());

            return round;
        }
    }
}
