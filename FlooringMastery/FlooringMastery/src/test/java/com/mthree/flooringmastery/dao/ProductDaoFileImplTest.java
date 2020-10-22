/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haroon
 */
public class ProductDaoFileImplTest {
    
     /**
     * The ProductDAO’s only job is to store and retrieve product information without altering
     * that data in any way. Tests will simply make sure that the DAO stores and retrieves products 
     * and their respective tax rates as advertised.
     * 
     * I will set up a fake product file to test the ProductDao
     * "Carpet,2.25,2.10"
     * "Laminate,1.75,2.10"
     * "Tile,3.50,4.15"
     *  
     */
    
    ProductDao testDao;
    
    public ProductDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() throws IOException, FlooringMasteryPersistenceException {
        
        String testFile = "TestFiles/TestData/TestProducts.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        
        String productsAsString = "ProductType::CostPerSquareFoot::LaborCostPerSquareFoot\n" +
                                  "Carpet::2.25::2.10\n" +
                                  "Laminate::1.75::2.10\n" +
                                  "Tile::3.50::4.15\n" +
                                  "Wood::2.14::3.17";
        
        //write our fake stock into our fake test tax file
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write(productsAsString);
        writer.close();
        
        testDao = new ProductDaoFileImpl(testFile);
        
    }
    

    @Test
    public void testGetProduct() throws FlooringMasteryPersistenceException {
        //1 - ARRANGE
        String productType = "Carpet";
        Product productCarpet = new Product(productType);
        productCarpet.setCostPerSquareFoot(new BigDecimal("2.25"));
        productCarpet.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        
        
        //2 - ACT
        Product retrievedProduct = testDao.getProduct(productType);
        
        //3 - ARRANGE
        assertEquals(productType, retrievedProduct.getProductType(), "Product Type should be Carpet");
        assertEquals(productCarpet.getCostPerSquareFoot(), retrievedProduct.getCostPerSquareFoot(), 
                "Cost per square foot should be 2.25");
        assertEquals(productCarpet.getLaborCostPerSquareFoot(), retrievedProduct.getLaborCostPerSquareFoot(),
                "Tax rate should be 4.45");
    }
    
    @Test
    public void testGetAllProduct() throws FlooringMasteryPersistenceException {
        //1 - ARRANGE
        String carpet = "Carpet";
        Product productCarpet = new Product(carpet);
        productCarpet.setCostPerSquareFoot(new BigDecimal("2.25"));
        productCarpet.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
    
        String productTypeLaminate = "Laminate";
        Product productLaminate = new Product(productTypeLaminate);
        productLaminate.setCostPerSquareFoot(new BigDecimal("1.75"));
        productLaminate.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        
        String tile = "Tile";
        Product productTile = new Product(tile);
        productTile.setCostPerSquareFoot(new BigDecimal("3.50"));
        productTile.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        
        
        //2 - ACT
        List<Product> allProductList = testDao.getAllProducts();
        
        //3 - ARRANGE
        assertNotNull(allProductList, "All product list should not be empty");

        assertEquals(4, allProductList.size(), "All product list should have 3 items");
        
        assertTrue(allProductList.contains(productCarpet), "All items list should contain Carpet");
        assertTrue(allProductList.contains(productLaminate), "All items list should contain Laminate");
        assertTrue(allProductList.contains(productLaminate), "All items list should contain item Tile");
    }
    
}
