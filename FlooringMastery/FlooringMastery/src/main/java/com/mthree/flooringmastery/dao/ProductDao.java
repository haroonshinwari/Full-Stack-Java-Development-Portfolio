/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface ProductDao {
    
    /**
     * Method returns a Product object containing the product information including cost per square foot
     * and labour cost per square foot, and is given by passing the string productType as the parameter
     * @param productType the key used in the hashmap to find the relative Product object
     * @return Product object containing the tax rate for that state as a big decimal
     * @throws FlooringMasteryPersistenceException if there is any issue reading from products.txt file
     */
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;
    
    
    /**
     * Method simply returns all the Product objects stored in the hashmap from reading the products file
     * @return a list of product objects, that can be used to filter  or display all the products
     * @throws FlooringMasteryPersistenceException if there is any issue reading from products.txt file
     */
    List<Product> getAllProducts() throws FlooringMasteryPersistenceException;
}
