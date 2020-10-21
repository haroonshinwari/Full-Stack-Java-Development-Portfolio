/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dao;

import com.mthree.vendingmachine.dto.Item;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface VendingMachineDao {
    
    
    /**
     * This method returns the item stored in the itemsHashmap given the itemNumber 
     * @param itemNumber for the requested item
     * @return the item in the hashmap stored under that itemNumber
     */
    Item getItem(int itemNumber) throws VendingMachinePersistenceException;
    
    /**
     * This method simply returns a list of all the items in the itemsHashMap
     * @return a list of item objects from the itemsHashmap
     */
    List<Item> getAllItems() throws VendingMachinePersistenceException;
    
    /**
     * This method only returns those items that are available, such that they have
     * an attribute greater than 0 for quantity in stock
     * @return a list of items that are in stock
     */
    List<Item> getAvailableItems() throws VendingMachinePersistenceException;
    
    /**
     * This method simply updates the itemsHashMap after an item has been purchased.
     * So that the inventory can be updated by decreasing the stock number for the given item.
     * returns null if item is out of stock or if item does not exist
     */
    Item updateInventory(Item item) throws VendingMachinePersistenceException;
    
}
