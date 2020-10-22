/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.*;
import com.mthree.flooringmastery.service.FlooringMasteryServiceLayer;
import com.mthree.flooringmastery.ui.ExitException;
import com.mthree.flooringmastery.ui.FlooringMasteryView;
import com.mthree.flooringmastery.ui.MainMenuException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class FlooringMasteryController {
    
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;
    private boolean keepGoing = true;
    
    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run(){
        int menuSelection = 0;
        startAudit();
        
        while (keepGoing) {
            menuSelection = getMenuSelection();
            
            switch (menuSelection) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    updateOrderStatus();
                    break;
                case 6:
                    exportData();
                    break;
                case 0:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
        exitAuditMessage("User selects to exit from the main menu");
    }
    
    
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private void displayOrders() {
        view.displayOrdersBanner();
        
        try {
            LocalDate deliveryDate = view.askUserForDeliveryDate("Please enter the delivery date in the format MM-DD-YYYY");
            List<Order> ordersList = service.getOrdersByDelivery(deliveryDate);
            view.displayOrders(ordersList);
            
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("There are no orders for this delivery date");
        }catch (MainMenuException ex) {
            return;
        } catch (ExitException exx) {
            exitAuditMessage("User exits from add order page");
            keepGoing = false;
            return;
        }
        
        mainOrExit();
        
        }
    
    private void addOrder() {
        view.displayAddOrderBanner();
        try {
            List<Product> listOfProducts = service.getAllProducts();
            List<StateTax> listOfStateTax = service.getAllStateTax();
            Order newOrder = view.getUserOrderInfo(listOfStateTax, listOfProducts);

            boolean confirmOrder = view.confirmOrder(newOrder);

            if (confirmOrder) {
                service.addOrder(newOrder.getDeliveryDate(), newOrder);
                view.displaySuccessAddOrder();
            } else {
                view.displayOrderNotAdded();
            }
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("Failed because the order could not be found or added to");
        } catch (MainMenuException ex) {
            return;
        } catch (ExitException exx) {
            exitAuditMessage("User exits from add order page");
            keepGoing = false;
            return;
        }
        mainOrExit();
    }
    
    
    private void editOrder() {
        view.displayEditOrderBanner();
        
        try {
            LocalDate deliveryDate = view.askUserForDeliveryDate("Please enter the delivery date of the order you would like to edit\n"
                + "Please enter the date in the format MM-DD-YYYY");
        
            int orderNumber = view.askUserForOrderNumber("Please enter the order number");
        
            Order retrievedOldOrder = service.getOrder(deliveryDate, orderNumber);
            if (retrievedOldOrder != null) {
                
                List<Product> listOfProducts = service.getAllProducts();
                List<StateTax> listOfStateTax = service.getAllStateTax();
                Order edittedOrder = view.getEditOrderInfo(retrievedOldOrder, listOfStateTax, listOfProducts);
                edittedOrder.setOrderNumber(retrievedOldOrder.getOrderNumber());
                edittedOrder.setOrderDate(retrievedOldOrder.getOrderDate());
                
                boolean confirmOrder = view.confirmEditedOrder(retrievedOldOrder, edittedOrder);
                if (confirmOrder) {
                    service.editOrder(deliveryDate, orderNumber, edittedOrder);
                    view.displaySuccessEditOrder();
                }else {
                view.displayOrderNotEdited();
                } 
                
           }else {
                view.displayErrorMessage("No such orders exists for the delivery date");
            }  
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("No orders exist at all for the delivery date");
        }catch (MainMenuException ex) {
            return;
        }catch (ExitException exx) {
            exitAuditMessage("User exits from edit order menu");
            keepGoing = false;
            return;
        }
        mainOrExit();
    }
    
    private void removeOrder() {
        view.displayRemoveOrderBanner();
           
        try {
            LocalDate deliveryDate = view.askUserForDeliveryDate("Please enter the delivery date of the order you would like to remove\n"
                + "Please enter the date in the format MM-DD-YYYY");
            int orderNumber = view.askUserForOrderNumber("Please enter the order number");
            
            Order retrievedOldOrder = service.getOrder(deliveryDate, orderNumber);
            
            boolean confirmRemovalOrder = view.confirmRemoveOrder(retrievedOldOrder);
            if (confirmRemovalOrder) {
                service.removeOrder(deliveryDate, orderNumber);
                view.displaySuccessRemoveOrder();
            }else {
                view.displayOrderNotRemoved();
            } 
            
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("No orders exist at all for this delivery date");
        }catch (MainMenuException ex) {
            return;
        }catch (ExitException exx) {
            exitAuditMessage("User exits from export order menu");
            keepGoing = false;
            return;
        }
        mainOrExit();
    }
    
    
    
    
    private void updateOrderStatus() {
        view.displayUpdateStatusBanner();
        LocalDate deliveryDate;
        try {
            deliveryDate = view.askUserForDeliveryDate("Please enter the delivery date of the order you would like to update status\n"
                + "Please enter the date in the format MM-DD-YYYY");
        
            int orderNumber = view.askUserForOrderNumber("Please enter the order number");
            
            
            Order retrievedOldOrder = service.getOrder(deliveryDate, orderNumber);
            if (retrievedOldOrder != null) {
                
                OrderStatus updatedOrderStatus = view.getUpdatedStatus(retrievedOldOrder);
                boolean confirmUpdateOrderStatus = view.confirmUpdateOrderStatus(retrievedOldOrder, updatedOrderStatus);
                
                if (confirmUpdateOrderStatus) {
                    if (retrievedOldOrder.getOrderStatus() == updatedOrderStatus) {
                        view.displayNoNeedToUpdateStatus();
                    }else {
                        service.updateStatus(deliveryDate, orderNumber, updatedOrderStatus);
                        view.displaySuccessUpdateOrder();
                    }
                }else {
                view.displayOrderStatusNotUpdated();
                } 
                
           }else {
                view.displayErrorMessage("No such orders exists for the delivery date: " + deliveryDate.format(DateTimeFormatter.ofPattern("d MMM u")));
            }  
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("No orders exist at all for the delivery date: ");
        }catch (MainMenuException ex) {
            return;
        }catch (ExitException exx) {
            exitAuditMessage("User exits from export order menu");
            keepGoing = false;
            return;
        }
        mainOrExit();
    }
    
    
    private void exportData() {
        view.displayExportDataBanner();
        try {
            
            boolean confirmExportData = view.confirmRemoveOrder();
            if (confirmExportData) {
                service.exportData();
                view.displaySuccessExportData();
            }else {
                view.displayDataNotExported();
            } 
            
        }catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage("There was an error writting the export file");
        }catch (MainMenuException ex) {
            return;
        }catch (ExitException exx) {
            exitAuditMessage("User exits from export order menu");
            keepGoing = false;
            return;
        }
        mainOrExit();
    }
    
    
    //**********************************************
    //*  >>>> >>>>    OTHER METHODS    <<<< <<<<  *
    //*********************************************

    
    private void mainOrExit() {  
        int mainOrExit = view.askUserMainOrExit();
        if (mainOrExit == 0) {
            keepGoing = false;
        }
    }

    public void unknownCommand(){
        view.displayUnkownCommandBanner();
    }
    
    public void exitMessage(){
        view.displayExitBanner();
    }
    
    public void exitAuditMessage(String exitMessage){
        
        try {
            service.exitAudit(exitMessage);
        }catch(FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    private void startAudit(){
        try {
            service.startAudit();
        }catch(FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
}