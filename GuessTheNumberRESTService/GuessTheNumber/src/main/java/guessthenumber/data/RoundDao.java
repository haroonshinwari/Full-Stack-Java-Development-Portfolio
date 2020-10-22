package guessthenumber.data;

import guessthenumber.models.Round;

import java.util.List;

public interface RoundDao {

    void addRound(Round round);
    List<Round> getRoundsByGameID(int id);
    List<Round> getAllRounds();
    void deleteRoundsByGameID(int gameID);
}
