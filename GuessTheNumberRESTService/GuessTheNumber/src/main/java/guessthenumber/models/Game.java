package guessthenumber.models;

public class Game {

    int gameID;
    String randomNumber;
    boolean finished;


    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameID != game.gameID) return false;
        if (finished != game.finished) return false;
        return randomNumber.equals(game.randomNumber);
    }

    @Override
    public int hashCode() {
        int result = gameID;
        result = 31 * result + randomNumber.hashCode();
        result = 31 * result + (finished ? 1 : 0);
        return result;
    }
}
