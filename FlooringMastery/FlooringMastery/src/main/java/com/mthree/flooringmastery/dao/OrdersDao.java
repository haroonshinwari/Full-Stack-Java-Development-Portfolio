/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.OrderStatus;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface OrdersDao {
    
    /**
     * This method returns an Order object based on the order date and order number entered by the user
     * Returns null if such an order does not exist
     * @param deliveryDate a valid date representing the delivery date
     * @param orderNumber the order number for a specific order for the relevant date
     * @return Order object that is valid for the given date and order number if it exists, null otherwise
     * @throws FlooringMasteryPersistenceException if order file could not be loaded
     */
    Order getOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException;
    
    
    /**
     * Method returns a list of Order object based on the delivery date
     * Returns null if no orders exist for given  delivery date
     * @param deliveryDate valid date representing the delivery date
     * @return List of Order objects for the given order date if they exist, null otherwise
     * @throws FlooringMasteryPersistenceException if order file could not be loaded
     */
    List<Order> getAllOrdersByDeliveryDate(LocalDate deliveryDate) throws FlooringMasteryPersistenceException;
    
    
    /**
     * Adds an order to memory and writes to file 
     * @param deliveryDate date in the format DDMMYYYY representing the delivery date
     * @param order Order object to be added to the Orders file for the given delivery date
     * @return Order that has now been added to memory and written to file, null otherwise
     * @throws FlooringMasteryPersistenceException if order file could not be loaded
     */
    Order addOrder(LocalDate deliveryDate, Order order) throws FlooringMasteryPersistenceException;
    
    
    /**
     * Removes an order based on orderDate and orderNumber
     * @param deliveryDate valid date representing the delivery date
     * @param orderNumber the order number for a specific order for the relevant date
     * @return Order object that is removed if it exists, null otherwise
     * @throws FlooringMasteryPersistenceException if order file could not be loaded to remove the order from
     */
    Order removeOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException ;
    
    
    /**
     * Edits the order based on orderDate and orderNumber 
     * @param deliveryDate valid date representing the delivery date of order to be edited
     * @param orderNumber the order number for a specific order for the relevant date
     * @param order the new Order object that is to replace the existing record
     * @return the new order that has been successfully updated, null otherwise
     * @throws FlooringMasteryPersistenceException if order file could not be loaded to edit the order
     */
    Order editOrder(LocalDate deliveryDate,int orderNumber, Order order) throws FlooringMasteryPersistenceException;
    
    
    /**
     * Updates the status of record that the user has selected by giving delivery date and order number
     * @param deliveryDate valid date in the format DDMMYYYY representing the delivery date
     * @param orderNumber the order number for a specific order for the relevant date
     * @param newStatus the new updated Status the selected order must have
     * @return Order object with an updated status
     * @throws FlooringMasteryPersistenceException if order file could not be loaded to update the status for
     */
    Order updateStatus(LocalDate deliveryDate, int orderNumber, OrderStatus newStatus) throws FlooringMasteryPersistenceException;
    
    
     /**
     * Method returns a list of all Orders that currently have an active order status
     * Returns null if there are no active orders in the system
     * @return List of all Order objects to be exported to be written to DataExport.txt
     * @throws FlooringMasteryPersistenceException if order file cannot write to the backup file
     */
    List<Order> exportActiveOrders() throws FlooringMasteryPersistenceException;
    
}
