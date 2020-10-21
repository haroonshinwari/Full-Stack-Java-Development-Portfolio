/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dto;

import java.math.BigDecimal;

/**
 *
 * @author Haroon
 */
public enum Coins {
    FIVE_PENCE(new BigDecimal("0.05").setScale(2)), 
    TEN_PENCE(new BigDecimal("0.10").setScale(2)), 
    TWENTY_PENCE(new BigDecimal("0.20").setScale(2)), 
    FIFTY_PENCE(new BigDecimal("0.50").setScale(2)), 
    ONE_POUND(new BigDecimal("1").setScale(2)),
    TWO_POUNDS(new BigDecimal("2").setScale(2));
    
    private final BigDecimal value;
    
    Coins(BigDecimal val) {
        this.value = val;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    
}
