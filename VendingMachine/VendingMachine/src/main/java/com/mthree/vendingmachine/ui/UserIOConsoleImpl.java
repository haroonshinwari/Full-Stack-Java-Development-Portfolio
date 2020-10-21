/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.ui;

import com.mthree.vendingmachine.dto.Change;
import com.mthree.vendingmachine.dto.Coins;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 *
 * @author Haroon
 */
public class UserIOConsoleImpl implements UserIO {
    
    final private Scanner console = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }
    
    @Override
    public void printSameLine(String msg) {
        System.out.print(msg);
    }

    @Override
    public String readString(String msgPrompt) {
        System.out.println(msgPrompt);
        return console.nextLine();
    }
    
    
    @Override
    public String readNonEmptyString(String msgPrompt) {
        boolean invalidInput = true;
        String userInput = " ";
        
        while(invalidInput) {
            System.out.println(msgPrompt);
            userInput = console.nextLine();
            if (userInput == null || userInput.isEmpty() || userInput.trim().isEmpty()) {
                System.out.println("You entered nothing. Please try again.");
            }else {
                invalidInput = false;
            }   
        }
        return userInput;
    }
    
    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(msgPrompt);
                // Get the input line, and try and parse
                num = Integer.parseInt(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    /**
     *
     * A slightly more complex method that takes in a message to display on the console, 
     * and continually reprompts the user with that message until they enter an integer
     * within the specified min/max range to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @param min - minimum acceptable value for return
     * @param max - maximum acceptable value for return
     * @return an integer value as an answer to the message prompt within the min/max range
     */
    @Override
    public int readInt(String msgPrompt, int min, int max) {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public void printChange(Change change) {
        print("Change due £" + change.getChangeDue());
        
        if (change.getNumberOfTwoPoundCoins() != 0) {
            print(Coins.TWO_POUNDS.toString() + " = " + change.getNumberOfTwoPoundCoins());
        }
        
        if (change.getNumberOfOnePoundCoins() != 0) {
            print(Coins.ONE_POUND.toString() + " = " + change.getNumberOfOnePoundCoins());
        }
        
        if (change.getNumberOfFiftyPence()!= 0) {
            print(Coins.FIFTY_PENCE.toString() + " = " + change.getNumberOfFiftyPence());
        }
        
        if (change.getNumberOfTwentyPence()!= 0) {
            print(Coins.TWENTY_PENCE.toString() + " = " + change.getNumberOfTwentyPence());
        }
        
        if (change.getNumberOfTenPence()!= 0) {
            print(Coins.TEN_PENCE.toString() + " = " + change.getNumberOfTenPence());
        }
        
        if (change.getNumberOfFivePence()!= 0) {
            print(Coins.FIVE_PENCE.toString() + " = " + change.getNumberOfFivePence());
        }
    }
}
