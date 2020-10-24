function gtnFetchAllGames() {
    hideEverything();

    fetch("http://localhost:8080/api/guessthenumber/game")
        .then(response => {
            if (response.status != 200) {
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").innerHTML = r
                });
            } else {
                document.getElementById("displayAllGames").style.display = "block";
                document.getElementById("tableGamesBody").innerHTML = "";
                response.json().then(r =>
                    r.forEach(game => {
                        console.log(`${game.gameID}, ${game.randomNumber}, ${game.finished}`);
                        addToGameTable(game)
                    }
                    )
                )
            }
        })
        .catch((err) => console.log("fail: " + err));
}

function gtnFetchRoundsByID() {
    hideEverything();

    let gameID = document.forms["roundForm"]["gameID"].value;
    fetch("http://localhost:8080/api/guessthenumber/rounds/" + gameID)
        .then(response => {
            if (response.status == 204) {
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").style.display = "block";
                    document.getElementById("errorMessage").innerHTML = "No rounds have been played for game with GameID: " + gameID;
                });
            } else if (response.status != 200) {
                console.log("we caught the error :)")
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").style.display = "block";
                    document.getElementById("errorMessage").innerHTML = r
                })
            } else {
                document.getElementById("displayRounds").style.display = "block";
                document.getElementById("tableRoundsBody").innerHTML = "";
                response.json().then(r =>
                    r.forEach(round => {
                        console.log(`${round.gameID}, ${round.roundID}, ${round.guess}, ${round.result}, ${round.timeOfRound}`);
                        addToRoundsTable(round)
                    }
                    )
                )
            }
        })
        .catch((err) => {
            console.log("fail: " + err);
            noGameIDEntered();
        })

}

function gtnFetchGameByID() {
    hideEverything();

    let gameID = document.forms["gameForm"]["gameID"].value;
    console.log(gameID);
    fetch("http://localhost:8080/api/guessthenumber/game/" + gameID)

        .then(response => {
            if (response.status != 200) {
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").style.display = "block";
                    document.getElementById("errorMessage").innerHTML = r
                });
            } else if (gameID == "") {
                noGameIDEntered();
            } else {
                document.getElementById("displayAllGames").style.display = "block";
                document.getElementById("tableGamesBody").innerHTML = "";
                response.json().then(game => {
                    console.log(`${game.gameID}, ${game.randomNumber}, ${game.finished}`);
                    addToGameTable(game)
                }
                )
            }
        })
        .catch((err) => console.log("fail: " + err));
}

function gtnFetchBeginNewGame() {
    hideEverything();

    fetch("http://localhost:8080/api/guessthenumber/begin", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })

        .then(response => {
            if (response.status != 201) {
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").style.display = "block";
                    document.getElementById("errorMessage").innerHTML = r
                })
            } else {
                response.json().then(newGameID => {
                    console.log(`${"New game created with Game ID: " + newGameID}`);
                    displayNewGameID(newGameID)
                }
                )
            }
        })
        .catch((err) => console.log("fail: " + err + "----"));
}



function gtnFetchGuess() {
    hideEverything();

    let gameID = document.forms["guessForm"]["gameID"].value;
    let guess = document.forms["guessForm"]["guess"].value;
    const data = { gameId: gameID, guess: guess };

    fetch("http://localhost:8080/api/guessthenumber/guess", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },

        body: JSON.stringify(data),
    })

        .then(response => {
            if (response.status != 201) {
                response.text().then(r => {
                    console.log(r);
                    document.getElementById("errorMessage").style.display = "block";
                    document.getElementById("errorMessage").innerHTML = r
                })
            }
            else {
                document.getElementById("displayRounds").style.display = "block";
                document.getElementById("tableRoundsBody").innerHTML = "";
                response.json().then(guess => {
                    console.log(`${guess.gameID}, ${guess.roundID}, ${guess.guess}, ${guess.result}, ${guess.timeOfRound}`);
                    addToRoundsTable(guess)
                }
                )
            }
        })
        .catch((err) => console.log("fail: " + err + "----"));
}

function addToGameTable(game) {
    let newGameRow = "<tr>" + "<td>" + game.gameID + "</td>"
        + "<td>" + game.randomNumber + "</td>"
        + "<td>" + game.finished + "</td>" + "</tr>";

    document.getElementById("tableGamesBody").innerHTML += newGameRow;
}

function addToRoundsTable(round) {
    let newRoundRow = "<tr>" + "<td>" + round.gameID + "</td>"
        + "<td>" + round.roundID + "</td>"
        + "<td>" + round.guess + "</td>"
        + "<td>" + round.result + "</td>"
        + "<td>" + round.timeOfRound + "</td>" + "</tr>";

    console.log(round);
    document.getElementById("tableRoundsBody").innerHTML += newRoundRow;
}

function hideEverything() {
    document.getElementById("displayAllGames").style.display = "none";
    document.getElementById("displayRounds").style.display = "none";
    document.getElementById("errorMessage").style.display = "none";
}

function displayNewGameID(newGameID) {
    document.getElementById("errorMessage").style.display = "block";
    document.getElementById("errorMessage").innerHTML = "New game created with game id: " + newGameID;
}

function displayErrorMessage(r) {
    document.getElementById("container").innerHTML = r;
}

function noGameIDEntered() {
    console.log("No GameID entered. Please enter a valid GameID");
    document.getElementById("errorMessage").style.display = "block";
    document.getElementById("errorMessage").innerHTML = "No GameID entered. Please enter a valid GameID"
}
