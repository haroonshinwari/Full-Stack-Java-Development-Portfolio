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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Haroon
 */
public class VendingMachineServiceLayerImplTest {
    
    /**
     * Service Layer not responsible for storing or retrieving Item objects. That is the job of the DAO
     * Here we are testing the business rules
     * 
     * We will use stubbed version of the DAO, instead of a file-based implementation
     * A stubbed version of a component simply returns canned data for any particular method call. 
     * We can set up a stubbed version of a component to act just about any way we want or need it to.
     * 
     */
    
    private VendingMachineServiceLayer service;
    
    public VendingMachineServiceLayerImplTest() {  
//        VendingMachineDao dao = new VendingMachineDaoStubImpl();
//        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();
//        
//        service = new VendingMachineServiceLayerImpl(dao, auditDao);
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service =  ctx.getBean("serviceLayer", VendingMachineServiceLayer.class);
    }
    
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testValidPurchase() throws VendingMachinePersistenceException {
        
        //ARRANGE
        service.addToBalance(6); //added £2 to balance
        
        //ACT
        try {
            service.buyItem(1);
        }catch( InsufficientFundsException 
                | NoItemInventoryException
                | InsufficientFundsForPurchaseException e) {
        //ASSERT
            fail("A valid item was bought with enough funds. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testInsufficientFundsForItem() throws VendingMachinePersistenceException {
        //Arrange
        service.addToBalance(5); //added £1 to balance
        
        //ACT
        try {
            service.buyItem(1); // item costs £1.50
            fail("Expected Insufficient Funds Exception was not thrown."); 
        }catch( NoItemInventoryException 
                | InsufficientFundsForPurchaseException e) {
        //ASSERT
            fail("Incorrect exception was thrown");
        }catch(InsufficientFundsException e) {
        }
    }
    

    @Test
    public void testNoStockOfItem() throws VendingMachinePersistenceException {
        //Arrange
        service.addToBalance(6); //added £2 to balance
        
        //ACT
        try {
            service.buyItem(2); // item is out of stock
            fail("Expected NoItemInventory Exception was not thrown.");
            
        }catch( InsufficientFundsException
                | InsufficientFundsForPurchaseException e) {
        //ASSERT
            fail("Incorrect exception was thrown");
        }catch(NoItemInventoryException e) {
        }
    }
    
    @Test
    public void testNotEnoughFundsForPurchase() throws VendingMachinePersistenceException {
        //Arrange
        service.addToBalance(1); //added 5 penceto balance
        
        //ACT
        try {
            service.buyItem(1); // item costs £1.50
            fail("Expected Insufficient funds for purchase Exception was not thrown.");
            
        }catch( NoItemInventoryException 
                | InsufficientFundsException e) {
        //ASSERT
            fail("Incorrect exception was thrown");
        }catch(InsufficientFundsForPurchaseException e) {
        }
    }
    
    @Test
    public void testAddBalance() throws VendingMachinePersistenceException {
        //ARRANGE
        service.addToBalance(1); //adds 5p to balance 
        service.addToBalance(3); //adds 20p to balance
        service.addToBalance(6); //adds £2 to balance
                
        
        //ACT
        BigDecimal actualBalance = service.getBalance();
        
        //ASSERT
        assertEquals(new BigDecimal("2.25"), actualBalance, "balance should be £2.25");
        assertNotEquals(new BigDecimal("1"), actualBalance, "balance should NOT be £1");
    }
    
    
    @Test
    public void testGetItem() throws VendingMachinePersistenceException {
        //ARRANGE
        Item onlyItem = new Item(1);
        onlyItem.setCost(new BigDecimal(1.50));
        onlyItem.setName("Test Item");
        onlyItem.setQuantityInStock(5);
        
        //ACT & ASSERT
        Item shouldBeTestItem = service.getItem(1);
        assertNotNull(shouldBeTestItem, "Getting item #1 should not be null.");
        assertEquals(onlyItem, shouldBeTestItem, "Item stored under #1 should be Test Item.");
        
        Item shouldBeNull = service.getItem(42);
        assertNull(shouldBeNull, "Getting #42 should be null");
    }
    
    
    @Test
    public void testGetAllItem() throws VendingMachinePersistenceException {
        //ARRANGE
        Item testItem = new Item(1);
        testItem.setCost(new BigDecimal(1.50));
        testItem.setName("Test Item");
        testItem.setQuantityInStock(5);
        
        Item outOfStockItem = new Item(2);
        outOfStockItem.setCost(new BigDecimal(0.50));
        outOfStockItem.setName("Test Item");
        outOfStockItem.setQuantityInStock(0);
        
        //ACT & ASSERT
        assertEquals(2, service.getAllItems().size(), "Should only have 2 items");
        assertTrue(service.getAllItems().contains(testItem), "All items list should contain test item ");
        assertTrue(service.getAllItems().contains(outOfStockItem), "All items should contain out of stock item");
    }
    
    @Test
    public void testGetAvailableItems() throws VendingMachinePersistenceException {
        //ARRANGE
        Item testItem = new Item(1);
        testItem.setCost(new BigDecimal(1.50));
        testItem.setName("Test Item");
        testItem.setQuantityInStock(5);
        
        Item outOfStockItem = new Item(2);
        outOfStockItem.setCost(new BigDecimal(0.50));
        outOfStockItem.setName("Test Item");
        outOfStockItem.setQuantityInStock(0);
        
        //ACT & ASSERT
        assertEquals(1, service.getAllAvailableItems().size(), "Available items, should only have 1 items");
        assertTrue(service.getAllAvailableItems().contains(testItem), "All items list should contain test item ");
        assertFalse(service.getAllAvailableItems().contains(outOfStockItem),
                "Available items should NOT contain out of stock item");
    }
    
    @Test
    public void testGetCheapestItem() throws VendingMachinePersistenceException {
        //ARRANGE
        Item testItem = new Item(1);
        testItem.setCost(new BigDecimal(1.50));
        testItem.setName("Test Item");
        testItem.setQuantityInStock(5);
        
        Item outOfStockItem = new Item(2);
        outOfStockItem.setCost(new BigDecimal(0.50));
        outOfStockItem.setName("Test Item");
        outOfStockItem.setQuantityInStock(0);
        
        //ACT & ASSERT
        //assertEquals(1, service.getAllAvailableItems().size(), "Available items, should only have 1 items");
        assertEquals(outOfStockItem.getCost(), service.getCheapestItemPrice() , "Cheapest item should be out of stock item price");
        assertNotEquals(testItem.getCost(), service.getCheapestItemPrice(),
                "Cheapest item should NOT be test item price");
    }
    
    
}
