/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
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
public class ProductDaoFileImpl implements ProductDao {
    
    public final String PRODUCTS_FILE;
    public static final String DELIMITER = "::";
    
    Map<String, Product> productsHashMap = new HashMap<>();
    
    
    public ProductDaoFileImpl() {
        PRODUCTS_FILE = "Data/Products.txt";
    }
    
    public ProductDaoFileImpl(String productTextFile) throws FlooringMasteryPersistenceException {
        PRODUCTS_FILE = productTextFile;
        loadProducts();
    }
    

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return productsHashMap.get(productType);
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        loadProducts();
        return new ArrayList<Product>(productsHashMap.values());
    }
    
    
    private Product unmarshallProduct(String productAsText){
        /* 
         productAsText is expecting a line read in from our file.
         For example, it might look like this:
         
        
         We then split that line on our DELIMITER - which we are using as :: 
         Leaving us with an array of Strings, stored in productToken.
         Which should look like this:
         ___________________
         |      |    |    |   
         |Carpet|2.25|2.10|
         |      |    |    |           
         ------------------
            [0]   [1]  [2] 
        */
        
        String[] productToken = productAsText.split(DELIMITER);

        // Given the pattern above, the state abbreviation in index 0 of the array.
        String productType = productToken[0];

        // Which we can then use to create a new product object to satisfy
        // the requirements of the product constructor.
        Product productFromFile = new Product(productType);
        
        productFromFile.setCostPerSquareFoot(new BigDecimal(productToken[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productToken[2]));
        
        return productFromFile;
    }
    
    
    
    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try {
            File file = new File(PRODUCTS_FILE);
            scanner = new Scanner(new FileInputStream(file));
        }catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                "-_- Could not load products data into memory.", e);
        }
    
    
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentProduct holds the most recent product unmarshalled
        Product currentProduct;
        // Go through PRODUCT_FILE line by line, decoding each line into a 
        // PRODUCT object by calling the unmarshallPRODUCT method.
        // Process while we have more lines in the file
       
        scanner.nextLine();
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Product object
            currentProduct = unmarshallProduct(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            productsHashMap.put(currentProduct.getProductType() , currentProduct);
        }
        scanner.close();
    }
    
}
