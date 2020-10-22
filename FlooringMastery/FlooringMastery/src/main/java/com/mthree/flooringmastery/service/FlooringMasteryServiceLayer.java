/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.OrderStatus;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.StateTax;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface FlooringMasteryServiceLayer {
    
    Order getOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException;
    
    List<Order> getOrdersByDelivery(LocalDate deliveryDate) throws FlooringMasteryPersistenceException;
    
    Order addOrder(LocalDate deliveryDate, Order newOrder) throws FlooringMasteryPersistenceException ;
    
    void removeOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException ;
    
    Order editOrder(LocalDate deliveryDate, int orderNumber, Order edittedOrder) throws FlooringMasteryPersistenceException ;
    
    Order updateStatus(LocalDate deliveryDate, int orderNumber, OrderStatus newStatus)throws FlooringMasteryPersistenceException ;
    
    void exportData()throws FlooringMasteryPersistenceException ; 
    
    Product getProduct(String productType) throws FlooringMasteryPersistenceException ;
    
    List<Product> getAllProducts()throws FlooringMasteryPersistenceException ;
    
    StateTax getStateTax(String stringAbbreviation)throws FlooringMasteryPersistenceException ;
    
    List<StateTax> getAllStateTax()throws FlooringMasteryPersistenceException ; 

    public void exitAudit(String exitMessage) throws FlooringMasteryPersistenceException;

    public void startAudit() throws FlooringMasteryPersistenceException;
            
            
    
    
}
