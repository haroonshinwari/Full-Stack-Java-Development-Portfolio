/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.controller;

import com.mthree.vendingmachine.dao.*;
import com.mthree.vendingmachine.dto.*;
import com.mthree.vendingmachine.service.*;
import com.mthree.vendingmachine.ui.*;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class VendingMachineController {
    
    private VendingMachineView view;
    private VendingMachineServiceLayer service;
    private boolean keepGoing = true;
    
    public VendingMachineController(VendingMachineServiceLayer service, VendingMachineView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() throws VendingMachinePersistenceException, NoItemInventoryException, InsufficientFundsException {
        int menuSelection = 0;
        startAudit();
        
        if (service.getAllAvailableItems().isEmpty() ) {
            view.displayVendingMachineOutOfStock();
            exitAuditMessage("Program closes as vending machine is out of stock - PLEASE RESTOCK");
            keepGoing = false;
        }
        
        while (keepGoing) {
            menuSelection = getMenuSelection();
            
            switch (menuSelection) {
                case 1:
                    getStock();
                    break;
                case 2:
                    addBalance();
                    break;
                case 3:
                    buyItem();
                    break;
                case 0:
                    keepGoing = false;
                    exitAuditMessage("User exits from the main menu");
                    break;
                default:
                    unknownCommand();
            }
        } 
    }
    

    private int getMenuSelection() throws VendingMachinePersistenceException {
        List<Item> availableItems = service.getAllAvailableItems();
        
        if (availableItems.isEmpty() ) {
            view.displayVendingMachineOutOfStock();
            keepGoing = false;
            exitAuditMessage("Program closes as vending machine is out of stock - PLEASE RESTOCK");
            return 99;
            
        }
        return view.printMenuAndGetSelection(availableItems, service.getBalance()); 
    }
    
    private void getStock() throws VendingMachinePersistenceException {
        view.displayListInventoryBanner();
        List<Item> inventory = service.getAllItems();
        view.displayStock(inventory);
        
        int mainOrExit = view.askUserMainOrExit();
        if (mainOrExit == 0) {
                keepGoing = false;
                exitAuditMessage("User exits from stock page");
        }
    }
    
    public void addBalance() throws VendingMachinePersistenceException {
        boolean keepGoing = true;
        
        while (keepGoing) {
            view.displayAddCoinsBanner();
            view.displayCoinMenu();
            view.displayBalance(service.getBalance());
            int userEntry = view.getUserInsertCoins();
            if (userEntry == 0) {
                keepGoing = false;
            }else {
                service.addToBalance(userEntry);
            }
        }
        
    }
    
    public void buyItem() throws VendingMachinePersistenceException, NoItemInventoryException, InsufficientFundsException {
        view.displayBuyItemBanner();
        boolean hasErrors = false;
        
        do {
            List<Item> availableItems = service.getAllAvailableItems();
            List<Item> allItems = service.getAllItems();
            int userBuyItemChoice = view.askUserForItemChoice(availableItems, allItems,service.getBalance());
            if (userBuyItemChoice != 0) {   
                try {
                    Item item = service.getItem(userBuyItemChoice);
                    Change changeDue = service.buyItem(userBuyItemChoice);     //where exceptions get thrown if there is a problem with purchase
                    view.displaySuccessfulTransactionResults(item);
                    
                    if (service.getBalance().equals(0)) {
                        view.displayNoChangeDue();
                    }else {
                        view.displayChange(changeDue);
                    }
                    
                    int mainOrExit = view.askUserMainOrExit();
                    if (mainOrExit == 0) {
                            keepGoing = false;
                            exitAuditMessage("User exits after purchasing an item");
                    }

                    hasErrors = false;
                    
                }catch (InsufficientFundsException e) {
                    view.displayInsufficicentFundsMessage(service.getBalance(), service.getItem(userBuyItemChoice));
                    hasErrors = true;
                }catch (NoItemInventoryException e) {
                    view.displayNoItemInventoryException(service.getItem(userBuyItemChoice));
                    hasErrors = true;
                }catch (InsufficientFundsForPurchaseException e) {
                    view.displayInsufficicentFundsForPurchaseMessage(service.getBalance(), service.getItem(userBuyItemChoice));
                    hasErrors = false;
                }        
            }else {
                hasErrors = false;
            }
        }while(hasErrors);
       
    }
    
    public void unknownCommand(){
        view.displayUnkownCommandBanner();
    }
    
    public void exitAuditMessage(String exitMessage) throws VendingMachinePersistenceException {
        service.exitAudit(exitMessage);
        view.displayExitBanner();
    }
    
    public void startAudit() throws VendingMachinePersistenceException {
        service.startAudit();
    }
    
}
