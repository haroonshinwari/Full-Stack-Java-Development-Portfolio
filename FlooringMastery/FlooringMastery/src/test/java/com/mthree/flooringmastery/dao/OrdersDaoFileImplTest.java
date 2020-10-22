/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.OrderStatus;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.StateTax;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haroon
 */
public class OrdersDaoFileImplTest {

    OrdersDao testDao;
    ProductDao productDao;
    TaxDao taxDao;

    public OrdersDaoFileImplTest() {
    }

    /**
     * In order to not impact the real Orders data, I will generate an empty
     * orders text file at the start of each test and load it with fake order
     * data. I will use this data to test each method in the dao.
     */
    @BeforeEach
    public void setUp() throws IOException, FlooringMasteryPersistenceException {

        String testFile = "TestFiles/TestOrders/Orders_01012005.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);

        String productsAsString = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::"
                + "CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total::OrderDate::Status::LastUpdatedDate\n"
                + "2::Doctor Who::WA::9.25::Wood::243.00::5.15::4.75::1251.45::1154.25::216.51::2622.21::11-02-2002::APPENDING::01-02-2003\n"
                + "3::Albert Einstein::KY::6.00::Carpet::217.00::2.25::2.10::488.25::455.70::56.64::1000.59::01-01-2003::IN_FULFILMENT::04-05-2006";

        //write our fake stock into our fake test tax file
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write(productsAsString);
        writer.close();

        productDao = new ProductDaoFileImpl("TestFiles/TestData/TestProducts.txt");
        taxDao = new TaxDaoFileImpl("TestFiles/TestData/TestTaxes.txt");

        testDao = new OrdersDaoFileImpl(productDao, taxDao, "TestFiles/TestOrders/", "TestFiles/TestBackup/TestDataExport.txt");
    }

    @Test
    public void testGetOrdersbyDeliveryDate() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(2);
        testOrderOne.setCustomerName("Doctor Who");

        StateTax stateTaxObject = taxDao.getStateTax("WA");
        stateTaxObject.setTaxRate(new BigDecimal("9.25"));
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Wood");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);

        //2 ACT
        List<Order> retrievedListOfOrders = testDao.getAllOrdersByDeliveryDate(deliveryDateOne);

        //3 ASSERT
        assertNotNull(retrievedListOfOrders, "Retrieved list should not be null");
        assertEquals(2, retrievedListOfOrders.size(), "Retrieved list should have 2 orders");

        assertTrue(retrievedListOfOrders.contains(testOrderOne), "retrievedListOfOrders should contain testOrderOne");

    }
    
    @Test
    public void testGetOrder() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(2);
        testOrderOne.setCustomerName("Doctor Who");

        StateTax stateTaxObject = taxDao.getStateTax("WA");
        stateTaxObject.setTaxRate(new BigDecimal("9.25"));
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Wood");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);

        //2 ACT
        Order retrievedOrder = testDao.getOrder(deliveryDateOne, 2);

        //3 ASSERT
        assertNotNull(retrievedOrder, "Retrieved list should not be null");
        assertTrue(retrievedOrder.equals(testOrderOne), "Retrieved list should be testOrderOne");

    }
    
    @Test
    public void testAddOrder() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderFive = new Order(deliveryDateOne);

        testOrderFive.setOrderNumber(5);
        testOrderFive.setCustomerName("Superman");

        StateTax stateTaxObject = taxDao.getStateTax("TX");
        stateTaxObject.setTaxRate(new BigDecimal("9.75"));
        testOrderFive.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Tile");
        productObject.setCostPerSquareFoot(new BigDecimal("6.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("8.75"));
        testOrderFive.setProduct(productObject);

        testOrderFive.setArea(new BigDecimal("143.00"));
        testOrderFive.setMaterialCost(new BigDecimal("2251.45"));
        testOrderFive.setLaborCosts(new BigDecimal("1234.50"));
        testOrderFive.setTax(new BigDecimal("317.83"));
        testOrderFive.setTotal(new BigDecimal("2822.93"));

        testOrderFive.setOrderDate(LocalDate.of(2003, Month.NOVEMBER, 24));
        testOrderFive.setOrderStatus(OrderStatus.APPENDING);
        testOrderFive.setLastUpdatedDate(LocalDate.of(2007, Month.MARCH, 13));

        //2 ACT
        //we have added the order into a file 
        testDao.addOrder(deliveryDateOne, testOrderFive);
        
        //we can retrieve the order to ensure that the file has been correctly uploaded
        Order retrievedOrder = testDao.getOrder(deliveryDateOne, 5);
        
        //3 ASSERT
        assertNotNull(retrievedOrder, "Added Order should not be null");
        
        assertEquals(5, retrievedOrder.getOrderNumber(), "added order number should be 5");
        assertEquals("Superman", retrievedOrder.getCustomerName(), "Added order customer name should be Superman");
        assertEquals("TX", retrievedOrder.getStateTax().getStateAbbreviation(),"State should be TX");
        assertEquals(new BigDecimal("9.75"), retrievedOrder.getStateTax().getTaxRate(), "Tax Rate should be 9.75");
        assertEquals("Tile", retrievedOrder.getProduct().getProductType(), "Product Type should be Tile");
        
        assertEquals(new BigDecimal("6.15"), retrievedOrder.getProduct().getCostPerSquareFoot(), "Cost per square foot should be 6.15");
        assertEquals(new BigDecimal("8.75"), retrievedOrder.getProduct().getLaborCostPerSquareFoot(), "Labor cost per square foot should be 8.75");
        assertEquals(new BigDecimal("143.00"), retrievedOrder.getArea(), "Area should be 143.00");
        assertEquals(new BigDecimal("2251.45"), retrievedOrder.getMaterialCost(), "Material cost should be 2251.45");
        assertEquals(new BigDecimal("1234.50"), retrievedOrder.getLaborCosts(), "Labor costs should be 1234.50");
        assertEquals(new BigDecimal("317.83"), retrievedOrder.getTax(), "Tax should be 317.83");
        assertEquals(new BigDecimal("2822.93"), retrievedOrder.getTotal(), "Total should be 2822.93");
        
        assertEquals(OrderStatus.APPENDING, retrievedOrder.getOrderStatus(), "Order Status should be appending");
        
        
        assertTrue(retrievedOrder.equals(testOrderFive), "Added order should equal testOrderFive");
    }
    
    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(2);
        testOrderOne.setCustomerName("Doctor Who");

        StateTax stateTaxObject = taxDao.getStateTax("WA");
        stateTaxObject.setTaxRate(new BigDecimal("9.25"));
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Wood");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);

        // ACT AND ASSERT
        List<Order> beforeRemoveList = testDao.getAllOrdersByDeliveryDate(deliveryDateOne);
        assertEquals(2, beforeRemoveList.size(), "Before remove, list size should be 2");
        assertTrue(beforeRemoveList.contains(testOrderOne), "Before remove list should contain testOrderOne");
        
        testDao.removeOrder(deliveryDateOne, 2);
        Order retrievedRemovedOrder = testDao.getOrder(deliveryDateOne, 2);
        assertNull(retrievedRemovedOrder, "retrieved order should be null now that it has been removed");
        

        List<Order> afterRemoveList = testDao.getAllOrdersByDeliveryDate(deliveryDateOne);
        assertEquals(1, afterRemoveList.size(), "After remove, list size should be 1");
        assertFalse(afterRemoveList.contains(testOrderOne), "After remove should not contain testOrderOne");
        
    }
    
    
    @Test
    public void testUpdateOrder() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(2);
        testOrderOne.setCustomerName("Doctor Who");

        StateTax stateTaxObject = taxDao.getStateTax("WA");
        stateTaxObject.setTaxRate(new BigDecimal("9.25"));
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Wood");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);

        // ACT
        testDao.updateStatus(deliveryDateOne, 2, OrderStatus.PAID);
        Order retrievedUpdatedOrder = testDao.getOrder(deliveryDateOne, 2);
        
        //ASSERT
        assertEquals(OrderStatus.PAID, retrievedUpdatedOrder.getOrderStatus(), "Order status should be PAID");
        assertNotEquals(OrderStatus.DELIVERED, retrievedUpdatedOrder.getOrderStatus(), "Order status should NOT be DELIVERED");
    }
    
    
    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2005, Month.JANUARY, 01);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(4);
        testOrderOne.setCustomerName("Batman"); //changed value from Doctor Who to Batman

        StateTax stateTaxObject = taxDao.getStateTax("TX"); //changed value from WA to TX
        stateTaxObject.setTaxRate(new BigDecimal("5.24")); //changed value
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Tile");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);
        testOrderOne.setLastUpdatedDate(LocalDate.now());

        // ACT AND ASSERT
        testDao.editOrder(deliveryDateOne, 2, testOrderOne);
        
        //we retrieve teh order to ensure it has been edited appropriately
        Order updatedOrder = testDao.getOrder(deliveryDateOne, 4);
        
        assertEquals("Batman", updatedOrder.getCustomerName(), "updated order should have a customer name Batman");
        assertEquals("TX", updatedOrder.getStateTax().getStateAbbreviation(), "Updated order should have TX as state");
        assertEquals(new BigDecimal("5.24"), updatedOrder.getStateTax().getTaxRate(), "Tax rate should be updated to 5.24");
        assertEquals(testOrderOne, updatedOrder, "The retrieved should be test order one");
        assertEquals("Batman", updatedOrder.getCustomerName(), "Retrieved updated order should be batman");
        
        
        
        
    }
    
    @Test
    public void testExportOrders() throws FlooringMasteryPersistenceException {

        //1 ARRANGE
        LocalDate deliveryDateOne = LocalDate.of(2011, Month.JANUARY, 25);
        deliveryDateOne.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrderOne = new Order(deliveryDateOne);

        testOrderOne.setOrderNumber(7);
        testOrderOne.setCustomerName("Batman"); //changed value from Doctor Who to Batman

        StateTax stateTaxObject = taxDao.getStateTax("TX"); //changed value from WA to TX
        stateTaxObject.setTaxRate(new BigDecimal("5.24")); //changed value
        testOrderOne.setStateTax(stateTaxObject);

        Product productObject = productDao.getProduct("Tile");
        productObject.setCostPerSquareFoot(new BigDecimal("5.15"));
        productObject.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        testOrderOne.setProduct(productObject);

        testOrderOne.setArea(new BigDecimal("243.00"));
        testOrderOne.setMaterialCost(new BigDecimal("1251.45"));
        testOrderOne.setLaborCosts(new BigDecimal("1154.25"));
        testOrderOne.setTax(new BigDecimal("216.51"));
        testOrderOne.setTotal(new BigDecimal("2622.21"));

        testOrderOne.setOrderDate(LocalDate.of(2002, Month.NOVEMBER, 02));
        testOrderOne.setOrderStatus(OrderStatus.APPENDING);
        testOrderOne.setLastUpdatedDate(LocalDate.of(2013, Month.DECEMBER, 18));
        
        
        // ACT
        testDao.addOrder(deliveryDateOne, testOrderOne); 
        
        testDao.exportActiveOrders();
            
            
        //ASSERT - check to ensure the export has been successfull by looking at the 
        //TestBackupFolder and checking the TestData Export.txt file.
        File backupFile = new File("TestFiles/TestBackup/TestDataExport.txt");
        
        assertTrue(backupFile.exists(), "The backup file should exist");
        
        
        
    }
    
}
