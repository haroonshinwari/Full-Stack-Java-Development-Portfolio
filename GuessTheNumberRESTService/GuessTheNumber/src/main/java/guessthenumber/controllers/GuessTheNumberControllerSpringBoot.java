package guessthenumber.controllers;


import guessthenumber.models.Game;
import guessthenumber.models.Guess;
import guessthenumber.models.Round;
import guessthenumber.service.ServiceLayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/guessthenumber")
public class GuessTheNumberControllerSpringBoot {

    private final ServiceLayer service;

    public GuessTheNumberControllerSpringBoot(ServiceLayer service) {
        this.service = service;
    }

    @GetMapping("/game")
    public ResponseEntity<List<Game>> displayAllGames() {
        List<Game> allGames = service.getAllGames();

        if (allGames.isEmpty()) {
            return new ResponseEntity("There are no games to display",null, HttpStatus.NO_CONTENT);
        }

        for (Game game : allGames) {
            if (!game.isFinished()) {
                game.setRandomNumber("XXXX");
            }
        }
        return ResponseEntity.ok(allGames);
    }


    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> displayGame(@PathVariable int gameId) {
        Game game = service.getGameByID(gameId);

        if (game == null) {
            return new ResponseEntity("No such game exists with this gameID: " + gameId, null, HttpStatus.NOT_FOUND);
        }

        if (!game.isFinished()) {
            game.setRandomNumber("XXXX");
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Round>> displayRounds(@PathVariable int gameId) {

        if (service.getGameByID(gameId) == null) {
            return new ResponseEntity("No such game exists with GameID: " + gameId, null, HttpStatus.NOT_FOUND);
        }

        List<Round> rounds = service.getRoundsByGameID(gameId);

        if (rounds.isEmpty()) {
            return new ResponseEntity("No rounds have been played for game with GameID: " + gameId, null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(rounds);
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {
        List<Integer> randomNumberList = service.getRandomDigit();

        StringBuffer sb = new StringBuffer();

        for (Integer i : randomNumberList) {
            sb.append(i);
        }

        Game newGame = new Game();
        newGame.setRandomNumber(sb.toString());
        newGame.setFinished(false);

        newGame = service.addGame(newGame);
        return newGame.getGameID();
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Round> checkGuess(@RequestBody Guess guess) {

        //missing gameId as part of guess
        if (guess.getGameId() == null ) {
            return new ResponseEntity("No GameID entered. Please enter a valid GameID",
                    null, HttpStatus.NOT_FOUND);
        }

        //missing guess as part of guess object
        if (guess.getGuess() == null ) {
            return new ResponseEntity("No guess entered. Please enter a valid guess",
                    null, HttpStatus.NOT_FOUND);
        }



        //check if game exists error
        if (service.getGameByID(guess.getGameId()) == null) {
            return new ResponseEntity("No game exists with GameID " + guess.getGameId() + ", please enter a valid gameID",
                    null, HttpStatus.NOT_FOUND);
        }

        //check if game is finished -then do not take any guesses
        boolean isGameFinished = service.getGameByID(guess.getGameId()).isFinished();
        if (isGameFinished) {
            return new ResponseEntity("Game with GameID " + guess.getGameId() + " is already finished. Not taking anymore guesses for this game",
                    null, HttpStatus.ALREADY_REPORTED);
        }

        //if guess is not of length 4
        if (guess.getGuess().length() > 4) {
            return new ResponseEntity("Too many values entered for the guess. Please enter a guess of 4 digits.\nInvalid Guess: " + guess.getGuess(),
                    null, HttpStatus.UNPROCESSABLE_ENTITY);
        }else if (guess.getGuess().length() < 4) {
            return new ResponseEntity("Not enough values entered for the guess. Please enter a guess of 4 digits.\nInvalid Guess: " + guess.getGuess(),
                    null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //check to ensure 4 characters are numbers
        try {
            Integer.parseInt(guess.getGuess());
        }catch(NumberFormatException e) {
            return new ResponseEntity("Invalid input. Please enter 4 numbers as part of your guess.\nInvalid Guess: " + guess.getGuess(), null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        String randomNumber = service.getGameByID(guess.getGameId()).getRandomNumber();
        String result = service.checkGuess(randomNumber, guess.getGuess());

        if (result.equals("e:4:p:0")) {
            service.updateGameFinished(guess.getGameId());
        }

        //create a new round object based on guess
        Round round = new Round();
        round.setGameID(guess.getGameId());
        round.setGuess(guess.getGuess());
        round.setResult(result);
        round.setTimeOfRound(LocalDateTime.now());
        List<Round> listOfRounds = service.getRoundsByGameID(guess.getGameId());

        int numberOfRounds = listOfRounds.size();
        round.setRoundID(numberOfRounds + 1);

        service.addRound(round);
        return new ResponseEntity(round, HttpStatus.CREATED);
    }
}
