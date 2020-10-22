/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.StateTax;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Haroon
 */
public class TaxDaoFileImpl implements TaxDao {
    
    public final String TAXES_FILE;
    public static final String DELIMITER = "::";
    
    Map<String, StateTax> stateTaxHashMap = new HashMap<>();
    
    
    public TaxDaoFileImpl() {
        TAXES_FILE = "Data/Taxes.txt";
    }
    
    public TaxDaoFileImpl(String taxTextFile) throws FlooringMasteryPersistenceException {
        TAXES_FILE = taxTextFile;
        loadTaxes();
    }
    
    @Override
    public StateTax getStateTax(String stateAbbreviation) throws FlooringMasteryPersistenceException {
        loadTaxes();
        return stateTaxHashMap.get(stateAbbreviation);
    }

    @Override
    public List<StateTax> getAllStateTax() throws FlooringMasteryPersistenceException {
        loadTaxes();
        return new ArrayList<StateTax>(stateTaxHashMap.values());
    }
    
    private StateTax unmarshallStateTax(String stateTaxAsText){
        /* 
         stateTaxAsText is expecting a line read in from our file.
         For example, it might look like this:
         TX::Texas::4.45
        
         We then split that line on our DELIMITER - which we are using as | (pipe)
         Leaving us with an array of Strings, stored in stateTaxToken.
         Which should look like this:
         _______________
         |  |     |    |   
         |TX|Texas|4.45|
         |  |     |    |           
         ---------------
          [0] [1]   [2] 
        */
        
        String[] stateTaxToken = stateTaxAsText.split(DELIMITER);

        // Given the pattern above, the state abbreviation in index 0 of the array.
        String stateAbbreviation = stateTaxToken[0];

        // Which we can then use to create a new StateTax object to satisfy
        // the requirements of the StateTax constructor.
        StateTax stateTaxFromFile = new StateTax(stateAbbreviation);
        
        stateTaxFromFile.setStateName(stateTaxToken[1]);
        
        
        
        
        BigDecimal taxRate = new BigDecimal(stateTaxToken[2]);
        stateTaxFromFile.setTaxRate(taxRate);
        
        return stateTaxFromFile;
    }
    
    
    
    private void loadTaxes() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            File file = new File(TAXES_FILE);
            scanner = new Scanner(new FileInputStream(file));
        }catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                "-_- Could not load taxes data into memory.", e);
        }
    
    
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentStateTax holds the most recent StateTax unmarshalled
        StateTax currentStateTax;
        // Go through LIBRARY_FILE line by line, decoding each line into a 
        // DVD object by calling the unmarshallDVD method.
        // Process while we have more lines in the file
       
        scanner.nextLine();
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a DVD object
            currentStateTax = unmarshallStateTax(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            stateTaxHashMap.put(currentStateTax.getStateAbbreviation() , currentStateTax);
        }
        scanner.close();
    }
    
}
