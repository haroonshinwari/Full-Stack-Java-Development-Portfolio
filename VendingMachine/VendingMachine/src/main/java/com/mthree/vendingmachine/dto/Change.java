/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Haroon
 */
public class Change {
    
    private int numberOfFivePence;
    private int numberOfTenPence;
    private int numberOfTwentyPence;
    private int numberOfFiftyPence;
    private int numberOfOnePoundCoins;
    private int numberOfTwoPoundCoins;
    Change changeDue;
    BigDecimal changeDueBigDecimal;
    
    
    
    public Change(BigDecimal change) {
        
        changeDueBigDecimal = change;
        
        numberOfTwoPoundCoins = change.divide(Coins.TWO_POUNDS.getValue(), 0, RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.TWO_POUNDS.getValue());
        
        numberOfOnePoundCoins = change.divide(Coins.ONE_POUND.getValue(), 0, RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.ONE_POUND.getValue());
        
        numberOfFiftyPence = change.divide(Coins.FIFTY_PENCE.getValue(), 2,RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.FIFTY_PENCE.getValue());
        
        numberOfTwentyPence = change.divide(Coins.TWENTY_PENCE.getValue(), 2, RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.TWENTY_PENCE.getValue());
        
        numberOfTenPence = change.divide(Coins.TEN_PENCE.getValue(), 2, RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.TEN_PENCE.getValue());
        
        numberOfFivePence = change.divide(Coins.FIVE_PENCE.getValue(), 2, RoundingMode.FLOOR).intValue();
        change = change.remainder(Coins.FIVE_PENCE.getValue());
        
    }

    public int getNumberOfFivePence() {
        return numberOfFivePence;
    }

    public int getNumberOfTenPence() {
        return numberOfTenPence;
    }

    public int getNumberOfTwentyPence() {
        return numberOfTwentyPence;
    }

    public int getNumberOfFiftyPence() {
        return numberOfFiftyPence;
    }

    public int getNumberOfOnePoundCoins() {
        return numberOfOnePoundCoins;
    }

    public int getNumberOfTwoPoundCoins() {
        return numberOfTwoPoundCoins;
    }

    public BigDecimal getChangeDue() {
        return changeDueBigDecimal;
    }
}
