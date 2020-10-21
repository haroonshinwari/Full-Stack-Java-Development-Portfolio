/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dto;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haroon
 */
public class ChangeTest {
    
    
    /**
     * TEST PLAN CASES
     * 
     * ZERO CHANGE - £0 : Should return 0 for all the coins 
     * 
     * SMALL CHANGE - £0.30: should give [£2:0, £1:0, 50p:0, 20p:1, 10p:1, 5p:0]
     *              - £0.85: should give [£2:0, £1:0, 50p:1, 20p:1, 10p:1, 5p:1]
     * 
     * LARGE CHANGE - £2.40: should give [£2:1, £1:0, 50p:0, 20p:2, 10p:0, 5p:0]
     *              - £7.05: should give [£2:3, £1:1, 50p:0, 20p:0, 10p:0, 5p:1]
     */
    

    @Test
    public void testZeroChange() {
        //1 ARRANGE
        BigDecimal changeDueBigDecimal = new BigDecimal(0);
        
        //2 - ACT
        Change changeDue = new Change(changeDueBigDecimal);
        
        //3 - ASSERT
        assertEquals(0, changeDue.getNumberOfFivePence(), "change due should be 0 five pence");
        assertEquals(0, changeDue.getNumberOfTenPence(), "change due should be 0 ten pence");
        assertEquals(0, changeDue.getNumberOfTwentyPence(), "change due should be 0 twenty pence");
        assertEquals(0, changeDue.getNumberOfFiftyPence(), "change due should be 0 fifty pence");
        assertEquals(0, changeDue.getNumberOfOnePoundCoins(), "change due should be 0 one pound");
        assertEquals(0, changeDue.getNumberOfTwoPoundCoins(), "change due should be 0 two pounds");
    }
    
    @Test
    public void testSmallChangeOne() {
        //1 ARRANGE
        BigDecimal changeDueBigDecimal = new BigDecimal("0.30");
        
        //2 - ACT
        Change changeDue = new Change(changeDueBigDecimal);
        
        //3 - ASSERT
        assertEquals(0, changeDue.getNumberOfFivePence(), "change due should be 0 five pence");
        assertEquals(1, changeDue.getNumberOfTenPence(), "change due should be 1 ten pence");
        assertEquals(1, changeDue.getNumberOfTwentyPence(), "change due should be 1 twenty pence");
        assertEquals(0, changeDue.getNumberOfFiftyPence(), "change due should be 0 fifty pence");
        assertEquals(0, changeDue.getNumberOfOnePoundCoins(), "change due should be 0 one pound");
        assertEquals(0, changeDue.getNumberOfTwoPoundCoins(), "change due should be 0 two pounds");
    }
    
    @Test
    public void testSmallChangeTwo() {
        //1 ARRANGE
        BigDecimal changeDueBigDecimal = new BigDecimal("0.85");
        
        //2 - ACT
        Change changeDue = new Change(changeDueBigDecimal);
        
        //3 - ASSERT
        assertEquals(1, changeDue.getNumberOfFivePence(), "change due should be 1 five pence");
        assertEquals(1, changeDue.getNumberOfTenPence(), "change due should be 1 ten pence");
        assertEquals(1, changeDue.getNumberOfTwentyPence(), "change due should be 1 twenty pence");
        assertEquals(1, changeDue.getNumberOfFiftyPence(), "change due should be 1 fifty pence");
        assertEquals(0, changeDue.getNumberOfOnePoundCoins(), "change due should be 0 one pound");
        assertEquals(0, changeDue.getNumberOfTwoPoundCoins(), "change due should be 0 two pounds");
    }
    
    @Test
    public void testLargeChangeOne() {
        //1 ARRANGE
        BigDecimal changeDueBigDecimal = new BigDecimal("2.40");
        
        //2 - ACT
        Change changeDue = new Change(changeDueBigDecimal);
        
        //3 - ASSERT
        assertEquals(0, changeDue.getNumberOfFivePence(), "change due should be 0 five pence");
        assertEquals(0, changeDue.getNumberOfTenPence(), "change due should be 0 ten pence");
        assertEquals(2, changeDue.getNumberOfTwentyPence(), "change due should be 2 twenty pence");
        assertEquals(0, changeDue.getNumberOfFiftyPence(), "change due should be 0 fifty pence");
        assertEquals(0, changeDue.getNumberOfOnePoundCoins(), "change due should be 0 one pound");
        assertEquals(1, changeDue.getNumberOfTwoPoundCoins(), "change due should be 1 two pounds");
    }
    
    @Test
    public void testLargeChangeTwo() {
        //1 ARRANGE
        BigDecimal changeDueBigDecimal = new BigDecimal("7.05");
        
        //2 - ACT
        Change changeDue = new Change(changeDueBigDecimal);
        
        //3 - ASSERT
        assertEquals(1, changeDue.getNumberOfFivePence(), "change due should be 1 five pence");
        assertEquals(0, changeDue.getNumberOfTenPence(), "change due should be 0 ten pence");
        assertEquals(0, changeDue.getNumberOfTwentyPence(), "change due should be 0 twenty pence");
        assertEquals(0, changeDue.getNumberOfFiftyPence(), "change due should be 0 fifty pence");
        assertEquals(1, changeDue.getNumberOfOnePoundCoins(), "change due should be 1 one pound");
        assertEquals(3, changeDue.getNumberOfTwoPoundCoins(), "change due should be 3 two pounds");
    }
    
}
