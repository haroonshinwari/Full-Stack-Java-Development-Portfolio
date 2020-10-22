/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.StateTax;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haroon
 */
public class TaxDaoFileImplTest {
    
    /**
     * The TaxDAO’s only job is to store and retrieve state and tax rate information without altering
     * that data in any way. Tests will simply make sure that the DAO stores and retrieves staes and their
     * respective tax rates as advertised.
     * 
     * I will set up a fake test file to test the TaxDao
     *  "TX::Texas::4.45\n" +
     *  "WA::Washington::9.25\n" +
     *  "KY::Kentucky::6.00";
     */
    
    TaxDao testDao;
    
    public TaxDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() throws IOException, FlooringMasteryPersistenceException {
        String testFile = "TestFiles/TestData/TestTaxes.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        
        String stateTaxesAsString = "State::StateName::TaxRate\n" +
                                    "TX::Texas::4.45\n" +
                                    "WA::Washington::9.25\n" +
                                    "KY::Kentucky::6.00\n";
        
        //write our fake stock into our fake test tax file
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write(stateTaxesAsString);
        writer.close();
        
        testDao = new TaxDaoFileImpl(testFile);
    }
    
    @Test
    public void testGetStateTax() throws FlooringMasteryPersistenceException {
        
        //1 - ARRANGE
        String stateAbbreviation = "TX";
        StateTax texasState = new StateTax(stateAbbreviation);
        texasState.setStateName("Texas");
        texasState.setTaxRate(new BigDecimal("4.45"));
        
        
        //2 - ACT
        StateTax retrievedStateTax = testDao.getStateTax(stateAbbreviation);
        
        //3 - ARRANGE
        assertEquals(stateAbbreviation, retrievedStateTax.getStateAbbreviation(), "State abbreviation should be TX");
        assertEquals(texasState.getStateName(), retrievedStateTax.getStateName(), "State name should be Texas");
        assertEquals(texasState.getTaxRate(), retrievedStateTax.getTaxRate(), "Tax rate should be 4.45");

    }
    
    @Test
    public void testGetAllStateTax() throws FlooringMasteryPersistenceException {
        //1 - ARRANGE
        String stateAbbreviation = "TX";
        StateTax texasState = new StateTax(stateAbbreviation);
        texasState.setStateName("Texas");
        texasState.setTaxRate(new BigDecimal("4.45"));
        
        String stateAbbreviationTwo = "WA";
        StateTax washingtonState = new StateTax(stateAbbreviationTwo);
        washingtonState.setStateName("Washington");
        washingtonState.setTaxRate(new BigDecimal("9.25"));
        
        String stateAbbreviationThree = "KY";
        StateTax kentuckyState = new StateTax(stateAbbreviationThree);
        kentuckyState.setStateName("Kentucky");
        kentuckyState.setTaxRate(new BigDecimal("6.00"));
        
        //2 - ACT
        List<StateTax> allStateTaxList = testDao.getAllStateTax();
        
        
        //3 - ARRANGE
        assertNotNull(allStateTaxList, "All state tax list should not be empty");

        assertEquals(3, allStateTaxList.size(), "All state tax list should have 3 items");
        
        assertTrue(allStateTaxList.contains(texasState), "All items list should contain Texas");
        assertTrue(allStateTaxList.contains(washingtonState), "All items list should contain item Washington");
        assertTrue(allStateTaxList.contains(kentuckyState), "All items list should contain item Kentucky");
        
    }
}
