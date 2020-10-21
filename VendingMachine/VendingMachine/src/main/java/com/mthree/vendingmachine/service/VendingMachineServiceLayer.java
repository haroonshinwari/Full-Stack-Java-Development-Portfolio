/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.service;

import com.mthree.vendingmachine.dto.Change;
import com.mthree.vendingmachine.dao.VendingMachinePersistenceException;
import com.mthree.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface VendingMachineServiceLayer {
    
     /**
     * This method returns the item stored in the itemsHashmap given the itemNumber
     * @param itemNumber for the requested item
     * @return the item in the hashmap stored under that itemNumber
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException
     */
    Item getItem(int itemNumber) throws VendingMachinePersistenceException;
    
    /**
     * This method simply returns a list of all the items in the itemsHashMap
     * @return a list of item objects from the itemsHashmap
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException
     */
    List<Item> getAllItems() throws VendingMachinePersistenceException;
    
    /**
     * This method only returns those items that are available, such that they have
     * an attribute greater than 0 for quantity in stock
     * @return a list of items that are in stock
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException
     */
    List<Item> getAllAvailableItems() throws VendingMachinePersistenceException;
    
    /**
     * This method simply updates the itemsHashMap after an item has been purchased.So that the inventory can be updated by decreasing the stock number for the given item
     * @param item
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException
     */
    void updateInventory(Item item) throws VendingMachinePersistenceException;
    
    
    /**
     * This method returns the cheapest Item price in the items hashmap
     * @return BigDecimal as all currency is used in type BigDecimal
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException
     */
    public BigDecimal getCheapestItemPrice() throws VendingMachinePersistenceException;
    
    /**
     * This method allows the user to insert coins ranging from 5p,10p,20p,50p,£1 to £2
     * The user selects which coin they want to add and keeps doing so until they exit
     * @param coinChoice this is the userChoice for 
     * @throws com.mthree.vendingmachine.dao.VendingMachinePersistenceException 
     */
    public void addToBalance(int coinChoice) throws VendingMachinePersistenceException;
    
    
    /**
     * This method allows the user  to purchase an item, with their given funds
     * @param itemNumber
     * @return Change object containing the change at the end of the transaction if successful
     * @throws VendingMachinePersistenceException
     * @throws NoItemInventoryException
     * @throws InsufficientFundsException
     * @throws InsufficientFundsForPurchaseException 
     */
    public Change buyItem(int itemNumber) throws VendingMachinePersistenceException, NoItemInventoryException, InsufficientFundsException, InsufficientFundsForPurchaseException;
   
    /**
     * Returns the current balance at this point in time stored as a BigDecimal
     * @return a BigDecimal object containing the current balance
     */
    public BigDecimal getBalance();
    
    public void startAudit() throws VendingMachinePersistenceException;
    
    public void exitAudit(String exitMessage) throws VendingMachinePersistenceException;
            
            
    
}
