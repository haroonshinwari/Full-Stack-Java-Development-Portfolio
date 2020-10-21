/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dao;

import com.mthree.vendingmachine.dto.Item;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haroon
 */
public class VendingMachineDaoFileImplTest {
        
    /**
     * The DAO’s only job is to store and retrieve item information without altering that data in any way. 
     * Tests will simply make sure that the DAO stores and retrieves items as advertised.
     * "1::0.45::Test A::5"
     * "2::1.55::Test B::5"
     * "3::1.20::Test C::8"
     * "4::0.40::Test D::3"
     * "5::1.10::Test E::0";
     */
    
    VendingMachineDao testDao;
    
    public VendingMachineDaoFileImplTest() {
    }
    
    /**
     *In order to not impact the real inventory, I will generate an empty inventory at the start
     *of each test and load it with fake inventory data. I will use this data to test each method   
     * in the dao.
     * @throws IOException 
     */
    @BeforeEach
    public void setUp() throws IOException, VendingMachinePersistenceException{
        String testFile = "testInventory.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        
        String inventoryAsString = "1::0.45::Test A::5\n"
                                 + "2::1.55::Test B::5\n"
                                 + "3::1.20::Test C::8\n"
                                 + "4::0.40::Test D::3\n"
                                 + "5::1.10::Test E::0";
        
        //write our fake stock into our fake test inventory
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write(inventoryAsString);
        writer.close();
        
        testDao = new VendingMachineDaoFileImpl(testFile);
    
    }
    

    @Test
    public void testGetItem() throws VendingMachinePersistenceException {
        
        //1 - ARRANGE
        int itemNumber = 1;
        Item itemOne = new Item(itemNumber);
        itemOne.setCost(new BigDecimal("0.45"));
        itemOne.setName("Test A");
        itemOne.setQuantityInStock(5);
        
        
        //2 - ACT
        Item retrievedItem = testDao.getItem(itemNumber);
        
        //3 - ARRANGE
        assertEquals(itemNumber, retrievedItem.getItemNumber(), "Checking item number");
        assertEquals(itemOne.getCost(), retrievedItem.getCost(), "Checking item cost");
        assertEquals(itemOne.getCost(), retrievedItem.getCost(), "Checking item name");
        assertEquals(itemOne.getQuantityInStock(), retrievedItem.getQuantityInStock(), "Checking item quantitiy");
    }
    
    
    @Test
    public void testGetAllAndGetAvailableItem() throws VendingMachinePersistenceException {
        
        //1 - ARRANGE
        int itemNumber = 1;
        Item itemOne = new Item(itemNumber);
        itemOne.setCost(new BigDecimal("0.45"));
        itemOne.setName("Test A");
        itemOne.setQuantityInStock(5);
        
        int itemNumberFive = 5;
        Item itemOutOfStock = new Item(itemNumberFive);
        itemOutOfStock.setCost(new BigDecimal("0.45"));
        itemOutOfStock.setName("Test E");
        itemOutOfStock.setQuantityInStock(0);
        
        //2 - ACT
        List<Item> allList = testDao.getAllItems();
        List<Item> availableList = testDao.getAvailableItems();
        
        
        //3 - ARRANGE
        assertNotNull(allList, "All list should not be empty");
        assertNotNull(availableList, "Available list should not be empty");
        assertEquals(5, allList.size(), "All list should have 5 items");
        assertEquals(4, availableList.size(), "All list should have 4 items");
        
        assertTrue(allList.contains(itemOne), "All items list should contain item 1");
        assertFalse(allList.contains(itemOutOfStock), "All items list should contain item 5");
        
        assertTrue(availableList.contains(itemOne), "Available items list should contain item 1");
        assertFalse(availableList.contains(itemOutOfStock), "Available items list should NOT contain item 5");
    }
    
    
    @Test
    public void testUpdatetem() throws VendingMachinePersistenceException {
        
        //1 - ARRANGE
        int itemNumber = 1;
        Item itemOne = new Item(itemNumber);
        itemOne.setCost(new BigDecimal("0.45"));
        itemOne.setName("Test A");
        itemOne.setQuantityInStock(5);
        
        int itemNumberFive = 5;
        Item itemOutOfStock = new Item(itemNumberFive);
        itemOutOfStock.setCost(new BigDecimal("0.45"));
        itemOutOfStock.setName("Test E");
        itemOutOfStock.setQuantityInStock(0);
        
        int itemNumberSix = 5;
        Item itemNotInHashMap = new Item(itemNumberFive);
        itemNotInHashMap.setCost(new BigDecimal("0.45"));
        itemNotInHashMap.setName("Test E");
        itemNotInHashMap.setQuantityInStock(0);
        
        //2 ACT
        Item updateOne = testDao.updateInventory(itemOne);
        Item updateOutOfStock = testDao.updateInventory(itemOutOfStock);
        Item updateItemNotInHashMap = testDao.updateInventory(itemNotInHashMap);
        
        //3 - ARRANGE
        assertEquals(4, updateOne.getQuantityInStock(), "Stock should have dropped from 5 to 4 now");
        assertNull(updateOutOfStock, "Should return null for item out of stock");
        assertNull(updateItemNotInHashMap, "Should return null for item not in hashmap");
    }
}
