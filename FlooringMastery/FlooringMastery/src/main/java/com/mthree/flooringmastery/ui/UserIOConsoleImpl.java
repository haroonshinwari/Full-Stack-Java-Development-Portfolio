/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.ui;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public String readString(String msgPrompt) throws MainMenuException, ExitException {
        System.out.println(msgPrompt);
        String stringValue = console.nextLine();
        
        if (stringValue.equalsIgnoreCase("main")) {
            throw new MainMenuException("User requests main menu when entering a stringl");
        }else if (stringValue.equalsIgnoreCase("exit")) {
            throw new ExitException("User requests exit program when entering a string");
        }
        
        return stringValue;
    }
    
    
    @Override
    public String readNonEmptyString(String msgPrompt) throws MainMenuException, ExitException {
        boolean invalidInput = true;
        String userInput = " ";
        
        while(invalidInput) {
            System.out.println(msgPrompt);
            userInput = console.nextLine();
            
            if (userInput.equalsIgnoreCase("main")) {
                throw new MainMenuException("User requests main menu when entering a string");
            }else if (userInput.equalsIgnoreCase("exit")) {
                throw new ExitException("User requests exit program when entering a string");
            }
            
            if (userInput == null || userInput.isEmpty() || userInput.trim().isEmpty()) {
                System.out.println("You entered nothing. Please try again.");
            }else {
                invalidInput = false;
            }   
        }
        return userInput;
    }
    
    @Override
    public int readInt(String msgPrompt) throws MainMenuException, ExitException{
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
    public int readInt(String msgPrompt, int min, int max) throws MainMenuException, ExitException {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }
    
    
    @Override
    public LocalDate readLocalDate(String msgPrompt) throws MainMenuException, ExitException {
        boolean invalidInput = true;
        LocalDate date = LocalDate.of(1994,12,01);
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(msgPrompt);
                //if user enters main or exit then throw these exceptions
                if (stringValue.equalsIgnoreCase("main")) {
                    throw new MainMenuException("User requests main menu when entering date");
                }else if (stringValue.equalsIgnoreCase("exit")) {
                    throw new ExitException("User requests exit program when entering date");
                }
                // Get the input line, and try and parse
                date = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("MM-dd-yyyy")); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (DateTimeParseException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please enter a valid date");
            }
        }
        return date;
    }
    
    
    @Override
    public BigDecimal readBigDecimal(String msgPrompt) throws MainMenuException, ExitException{
        boolean invalidInput = true;
        BigDecimal numBigDecimal = new BigDecimal("0");
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(msgPrompt);
                if (stringValue.equalsIgnoreCase("main")) {
                    throw new MainMenuException("User requests main menu when entering big decimal");
                }else if (stringValue.equalsIgnoreCase("exit")) {
                    throw new ExitException("User requests exit program when entering big decimal");
                }
                // Get the input line, and try and parse
                numBigDecimal = new BigDecimal(stringValue).setScale(2,RoundingMode.HALF_UP);// if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            }catch (NumberFormatException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please enter a valid number to 2 decimal places");
            }catch(ArithmeticException e) {
                this.print("Please enter the area value to 2 decimal places eg : 100 would be 100.00");
            } 
        }
        return numBigDecimal;
    }

}
