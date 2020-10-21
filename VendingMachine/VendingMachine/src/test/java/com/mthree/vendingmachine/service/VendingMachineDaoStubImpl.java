/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.service;

import com.mthree.vendingmachine.dao.VendingMachineDao;
import com.mthree.vendingmachine.dao.VendingMachinePersistenceException;
import com.mthree.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao {
    
    public Item testItem;
    public Item outOfStockItem;
    
    public VendingMachineDaoStubImpl() {
        testItem = new Item(1);
        testItem.setCost(new BigDecimal(1.50));
        testItem.setName("Test Item");
        testItem.setQuantityInStock(5);
        
        outOfStockItem = new Item(2);
        outOfStockItem.setCost(new BigDecimal(0.50));
        outOfStockItem.setName("Test Item");
        outOfStockItem.setQuantityInStock(0);
        
    }
    
    public VendingMachineDaoStubImpl(Item testItem, Item outOfStockItem) {
        this.testItem = testItem;
        this.outOfStockItem = outOfStockItem;
    }

    @Override
    public Item getItem(int itemNumber) throws VendingMachinePersistenceException {
        if (itemNumber == testItem.getItemNumber()) {
            return testItem;
        } else if (itemNumber == outOfStockItem.getItemNumber()) {
            return outOfStockItem;
        }else {
            return null;
        }
    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        List<Item> allItemsList = new ArrayList<>();
        allItemsList.add(testItem);
        allItemsList.add(outOfStockItem);
        return allItemsList;
    }

    @Override
    public List<Item> getAvailableItems() throws VendingMachinePersistenceException {
        List<Item> availableItemsList = new ArrayList<>();
        availableItemsList.add(testItem);
        return availableItemsList;
    }


    @Override
    public Item updateInventory(Item item) throws VendingMachinePersistenceException {
        int newStock = testItem.getQuantityInStock() - 1;
        testItem.setQuantityInStock(newStock);
        return testItem;
    }
    
}
