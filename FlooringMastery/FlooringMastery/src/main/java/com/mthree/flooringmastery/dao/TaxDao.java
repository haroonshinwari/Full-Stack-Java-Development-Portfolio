/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.StateTax;
import java.util.List;


/**
 *
 * @author Haroon
 */
public interface TaxDao {
    
    /**
     * Method returns a StateTax object containing the tax rate and state name, and is given 
     * by passing the string abbreviation as the parameter
     * @param stringAbbreviation the key used in the hashmap to find the relative StateTax object
     * @return StateTax object containing the tax rate for that state as a big decimal
     * @throws FlooringMasteryPersistenceException if there is any issue reading from taxes.txt file
     */
    StateTax getStateTax(String stringAbbreviation) throws FlooringMasteryPersistenceException;
    
    
    /**
     * This method simply returns all the StateTax objects stored in the hashmap from reading the taxes file
     * @return a list of state tax objects, that can be used to filter  or display all the states and tax rates
     * @throws FlooringMasteryPersistenceException if there is any issue reading from taxes.txt file
     */
    List<StateTax> getAllStateTax() throws FlooringMasteryPersistenceException;
    
}
