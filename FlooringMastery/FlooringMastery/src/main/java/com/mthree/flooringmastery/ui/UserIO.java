/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Haroon
 */
public interface UserIO {
    
    /**
     * A very simple method that takes in a message to display on the console 
     * and then waits for a integer answer from the user to return.
     *
     * @param msg - String of information to display to the user.
     */
    void print(String msg);
    
    /**
     * A very simple method that takes in a message to display on the console 
     * and then waits for a integer answer from the user to return.
     *
     * @param msg - String of information to display to the user.
     *
     */
    void printSameLine(String msg);
    
    /**
     *
     * A simple method that takes in a message to display on the console, 
     * and then waits for an answer from the user to return.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as string
     * @throws com.mthree.flooringmastery.ui.MainMenuException
     * @throws com.mthree.flooringmastery.ui.ExitException
     */
    String readString(String msgPrompt) throws MainMenuException, ExitException;
    
    /**
     * 
     * @param prompt
     * @return 
     * @throws com.mthree.flooringmastery.ui.MainMenuException 
     * @throws com.mthree.flooringmastery.ui.ExitException 
     */
    String readNonEmptyString(String prompt) throws MainMenuException, ExitException;
    

    
    /**
     *
     * A simple method that takes in a message to display on the console, 
     * and continually reprompts the user with that message until they enter an integer
     * to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as integer
     * @throws com.mthree.flooringmastery.ui.MainMenuException
     * @throws com.mthree.flooringmastery.ui.ExitException
     */
    int readInt(String msgPrompt) throws MainMenuException, ExitException;
    
    
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
     * @throws com.mthree.flooringmastery.ui.MainMenuException
     * @throws com.mthree.flooringmastery.ui.ExitException
     */
    int readInt(String msgPrompt, int min, int max) throws MainMenuException, ExitException;
    
    /**
     * 
     * @param msgPrompt
     * @return 
     * @throws com.mthree.flooringmastery.ui.MainMenuException 
     * @throws com.mthree.flooringmastery.ui.ExitException 
     */
    public LocalDate readLocalDate(String msgPrompt) throws MainMenuException, ExitException;
    
    /**
     * 
     * @param msgPrompt
     * @return 
     * @throws com.mthree.flooringmastery.ui.MainMenuException 
     * @throws com.mthree.flooringmastery.ui.ExitException 
     */
    public BigDecimal readBigDecimal(String msgPrompt) throws MainMenuException, ExitException;
    
}
