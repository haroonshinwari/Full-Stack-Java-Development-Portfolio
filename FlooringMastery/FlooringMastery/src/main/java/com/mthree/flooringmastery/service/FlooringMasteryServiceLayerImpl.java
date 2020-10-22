/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.dao.OrdersDao;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.OrderStatus;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.StateTax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    
    private OrdersDao ordersDao;
    private ProductDao productDao;
    private TaxDao taxDao;
    private FlooringMasteryAuditDao auditDao;
    BigDecimal balance = new BigDecimal(0);

    public FlooringMasteryServiceLayerImpl(OrdersDao ordersDao,ProductDao productDao,TaxDao taxDao, FlooringMasteryAuditDao auditDao) {
        this.ordersDao = ordersDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.auditDao = auditDao;  
    }

    @Override
    public Order getOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException {
        return ordersDao.getOrder(deliveryDate, orderNumber);
    }
    
    @Override
    public List<Order> getOrdersByDelivery(LocalDate deliveryDate) throws FlooringMasteryPersistenceException {
        return ordersDao.getAllOrdersByDeliveryDate(deliveryDate);
    }

    @Override
    public Order addOrder(LocalDate deliveryDate, Order newOrder) throws FlooringMasteryPersistenceException  {
        ordersDao.addOrder(deliveryDate, newOrder);
        auditDao.writeAuditEntry("Successfully added a new Order");
        auditDao.writeAuditOrderEntry(newOrder);
        return newOrder;
    }

    @Override
    public void removeOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException  {
        Order removedOrder = getOrder(deliveryDate, orderNumber);
        ordersDao.removeOrder(deliveryDate, orderNumber);
        auditDao.writeAuditEntry("Removed Order [#" + orderNumber + "]  [" + deliveryDate.format(DateTimeFormatter.ofPattern("dd MMM u")) + "]");
        auditDao.writeAuditOrderEntry(removedOrder);
    }

    @Override
    public Order editOrder(LocalDate deliveryDate, int orderNumber, Order edittedOrder) throws FlooringMasteryPersistenceException  {
        ordersDao.editOrder(deliveryDate, orderNumber, edittedOrder);
        auditDao.writeAuditEntry("Edited Order [#" + orderNumber + "]  [" + deliveryDate.format(DateTimeFormatter.ofPattern("dd MMM u")) + "]" );
        auditDao.writeAuditOrderEntry(edittedOrder);
        return edittedOrder;
    }

    @Override
    public Order updateStatus(LocalDate deliveryDate, int orderNumber, OrderStatus newStatus) throws FlooringMasteryPersistenceException {
        String oldOrderStatusString = getOrder(deliveryDate, orderNumber).getOrderStatus().toString();
        ordersDao.updateStatus(deliveryDate, orderNumber, newStatus);
        auditDao.writeAuditEntry("Updated status from (" + oldOrderStatusString + ") to " + newStatus.toString() +
                " for [Order #" + orderNumber + "] [" + deliveryDate.format(DateTimeFormatter.ofPattern("dd MMM u")) + "]" );
        Order updatedOrder = ordersDao.getOrder(deliveryDate, orderNumber);
        return updatedOrder;
    }

    @Override
    public void exportData() throws FlooringMasteryPersistenceException  {
        ordersDao.exportActiveOrders();
        auditDao.writeAuditEntry("Successfully exported all ACTIVE orders to Backup/DataExport.txt");
    }

    @Override
    public Product getProduct(String productType)throws FlooringMasteryPersistenceException  {
        return productDao.getProduct(productType);
    }

    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException  {
        return productDao.getAllProducts();
    }

    @Override
    public StateTax getStateTax(String stringAbbreviation)throws FlooringMasteryPersistenceException  {
        return  taxDao.getStateTax(stringAbbreviation);
    }

    @Override
    public List<StateTax> getAllStateTax()throws FlooringMasteryPersistenceException  {
        return taxDao.getAllStateTax();
    }

    @Override
    public void startAudit() throws FlooringMasteryPersistenceException {
        auditDao.writeAuditEntry("====================== START OF TRANSACTION ======================\n");
        
    }
    
        @Override
    public void exitAudit(String exitMessage) throws FlooringMasteryPersistenceException {
        auditDao.writeAuditEntry(exitMessage);
        auditDao.writeAuditEntry("====================== END OF TRANSACTION ======================\n\n");
    }
}
