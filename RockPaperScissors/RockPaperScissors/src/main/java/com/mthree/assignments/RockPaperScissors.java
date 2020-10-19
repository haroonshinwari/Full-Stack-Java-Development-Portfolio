/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.assignments;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Haroon
 */
public class RockPaperScissors {
    
    public static void main(String[] args) {
        
        boolean playAgain = true;
        int numberOfTieRounds = 0;
        int numberOfRoundsUserWon = 0;
        int numberOfRoundsComputerWon = 0;
        
        
        do {
            int[] scoresArray = {numberOfTieRounds, numberOfRoundsUserWon, numberOfRoundsComputerWon};
            System.out.println("========== By Haroon Shinwari ==========");
            System.out.println("Welcome to my lovely game of Rock Paper Scissors");
            System.out.println("========== ------------------ ==========");
            
            int numberOfRounds = askUserRounds("How many rounds would you like to play? Minimum rounds: 1 and Maximum rounds: 10");
            
            for (int i = 1; i < numberOfRounds + 1; i++) {
                
                int userChoice = askUserChoice("Please pick Rock, Paper or Scissors [Rock = 1, Paper = 2, Scissors = 3]", i);
                int computerChoice = askComputer();
                
                decideRound(userChoice, computerChoice, scoresArray);
            }
            
            displayFinalResults(scoresArray);
            
            playAgain = askUserToPlayAgain("Would you like to play again. Please enter yes or no"); 
            
        }while(playAgain);  
        System.out.println("Thank you for playing!");
    }
    
    /**
     * This method asks the user the number of rounds they wish to play.
     * This method ensures that the input is in the required range of 1 - 10.
     * @param prompt A string prompt that is print to console
     * @return An integer value stating the number of rounds the user wishes to play (This is between 1 and 10)
     */
    public static int askUserRounds(String prompt) {
        boolean inputValid = true;
        int numberOfRounds = 0;
        Scanner scannerObject = new Scanner(System.in);
        
        //System.out.println(prompt);
        //numberOfRounds = Integer.parseInt(scannerObject.next());
        
        while (inputValid) {
            try{
                System.out.println(prompt);
                numberOfRounds = scannerObject.nextInt();
                if (numberOfRounds <0 || numberOfRounds > 10) {        //input is valid
                    System.out.println("Number entered outside range. Please enter a number between 1 and 10");
                    //scannerObject.next();
                }else {
                    inputValid = false;
                }  
            }catch(Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 10." );
                scannerObject.next(); //this consumes the invalid token
            }
        }
        return numberOfRounds;
    }
    
    /**
     * Asks the user to pick rock paper or scissors by entering either 1,2 or 3
     * [Rock = 1, Paper = 2, Scissors = 3]
     * @param prompt String printed to prompt user to make a choice
     * @param roundNumber This is the round number
     * @return An integer value reflecting the choice of rock paper or scissors
     */
    public static int askUserChoice(String prompt, int roundNumber) {
        boolean inputValid = true;
        int userChoice = 0;
        Scanner scannerObject = new Scanner(System.in);
        
        while (inputValid) {
            try{
                System.out.println("========== Round " + roundNumber + "==========");
                System.out.println(prompt);
                userChoice = scannerObject.nextInt();
                
                if (userChoice < 1 || userChoice > 3) {        //input is invalid
                    System.out.println("Number entered outside range. Please enter a number between 1 and 3");
                    //scannerObject.next();
                }else {
                    inputValid = false;
                }
            }catch(Exception e) {
            System.out.println("Invalid input. Please enter a number." );
            scannerObject.next(); //this consumes the invalid token
            }
        }
        return userChoice;
    }
    
    /**
     * This method makes the computer make a random choice between 1, 2 or 3 
     * to represent the decision to pick rock paper or scissors
     * @return An integer representing the computer's choice 
     */
    public static int askComputer() {
        Random computerRandom = new Random();
        return computerRandom.nextInt(3) + 1;
    }
    
    /**
     * This method decides the outcome of the round based on the user's choice and the 
     * computer's random choice
     * @param userChoice User's choice for this round
     * @param computerChoice Computer's random choice for this round
     * @param scores Array of scores to keep a tally, this gets updated at the end of each round
     * @return int[] Array of scores that are updated at the end of the round
     */
    public static int [] decideRound(int userChoice, int computerChoice, int [] scores) {
        System.out.println("You pick " + numberToString(userChoice));
        System.out.println("Computer picks " + numberToString(computerChoice) );
        if (userChoice == computerChoice ) {
            scores[0]+= 1;
            System.out.println("Round TIED");
        }else if((userChoice == 1 && computerChoice == 3) || (userChoice == 2 && computerChoice == 1) 
            || (userChoice == 3 && computerChoice == 2)) { 
            scores[1]+= 1;
            System.out.println("YOU WIN");
        }else {
            System.out.println("Computer wins !!!");
            scores[2]+= 1;
        }
        
        System.out.println("\nRounds tied: " + scores[0]);
        System.out.println("Rounds you won: " + scores[1]);
        System.out.println("Rounds computer won: " + scores[2]);
        return scores;
    }
   
    
    /**
     * Converts number choices to string choices 
     * @param number User or computer's choice of rock paper or scissors as an integer
     * @return String representation of the choices made 
     */
    public static String numberToString(int number) {
        
        if (number == 1) {
            return "Rock";
        }else if (number == 2) {
            return "Paper"; 
        }
        return "Scissors";
    }
    
    /**
     * This method concludes the final result of the game by using the scores array
     * @param scores array of scores to conclude final decision
     */
    public static void displayFinalResults(int[] scores) {
       System.out.println("========== GAME OVER ==========");
        
        System.out.println("Number of rounds tied: " + scores[0]);
        System.out.println("Number of rounds you won: " + scores[1]);
        System.out.println("Number of rounds computer won: " + scores[2]);
        
        if (scores[1] == scores[2]) {
            System.out.println("Match Tied"); 
        }else if (scores[1] > scores[2]) {
            System.out.println("Congratulations! You have won the game :)");
        }else {
            System.out.println("It's a shame. This time the computer has beaten you :(");
        }
    }
    
    /**
     * Ask the user if they want to play again
     * checks for invalid input
     * @param prompt Message for user and instructions for deciding to play again
     * @return boolean Flag to determine if they want to play again
     */
    public static boolean askUserToPlayAgain(String prompt) {

        boolean playAgain = true;
        Scanner scannerObject = new Scanner(System.in);
        System.out.println(prompt);
        
        while (playAgain) {
            switch (scannerObject.next()) {   
                case "yes" -> {
                    return playAgain;
                }
                case "no" -> {
                    scannerObject.close();
                    return !playAgain;
                }
                default -> System.out.println("please enter yes or no");
            }
        } 
        return !playAgain;
    } 
    
}
