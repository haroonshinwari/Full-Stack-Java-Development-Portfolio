package guessthenumber.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Round {

    int gameID;
    int roundID;
    String guess;
    String result;
    LocalDateTime timeOfRound;

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getRoundID() {
        return roundID;
    }

    public void setRoundID(int roundID) {
        this.roundID = roundID;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getTimeOfRound() {
        return timeOfRound;
    }

    public void setTimeOfRound(LocalDateTime timeOfRound) {
        this.timeOfRound = timeOfRound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Round round = (Round) o;

        if (gameID != round.gameID) return false;
        if (roundID != round.roundID) return false;
        if (!guess.equals(round.guess)) return false;
        if (!result.equals(round.result)) return false;
        return timeOfRound.equals(round.timeOfRound);
    }

    @Override
    public int hashCode() {
        int result1 = gameID;
        result1 = 31 * result1 + roundID;
        result1 = 31 * result1 + guess.hashCode();
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + timeOfRound.hashCode();
        return result1;
    }
}
