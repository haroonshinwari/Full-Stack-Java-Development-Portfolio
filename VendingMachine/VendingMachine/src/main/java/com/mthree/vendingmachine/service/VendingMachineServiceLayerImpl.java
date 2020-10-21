/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.service;


import com.mthree.vendingmachine.dto.Coins;
import com.mthree.vendingmachine.dao.VendingMachineDao;
import com.mthree.vendingmachine.dao.VendingMachinePersistenceException;
import com.mthree.vendingmachine.dto.Change;
import com.mthree.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Haroon
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {

    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;
    BigDecimal balance = new BigDecimal(0);

    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;  
        

    }
    
    @Override
    public Item getItem(int itemNumber) throws VendingMachinePersistenceException {
        return dao.getItem(itemNumber);
    }
    
    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        return dao.getAllItems();
    }

    @Override
    public List<Item> getAllAvailableItems() throws VendingMachinePersistenceException {
        return dao.getAvailableItems();
    }
    
    @Override
    public BigDecimal getCheapestItemPrice() throws VendingMachinePersistenceException {
        List<Item> allItems = dao.getAllItems();
        
        Optional<BigDecimal> cheapestItemOptional = allItems.stream()
                .map(Item::getCost)
                .min(Comparator.naturalOrder());
        
        BigDecimal cheapestItem = cheapestItemOptional.get();
          
        return cheapestItem;
    }
    
    //NEED TO THINK ABOUT THIS COULD BE REDUNDANT LOL
    @Override
    public void updateInventory(Item item) throws VendingMachinePersistenceException {
        dao.updateInventory(item);
    }
    
    
    @Override
    public Change buyItem(int itemNumber) throws VendingMachinePersistenceException, NoItemInventoryException, InsufficientFundsException, InsufficientFundsForPurchaseException {
        Item item = getItem(itemNumber);
        
        //check if user can afford cheapest item in the vending machine
        if (getCheapestItemPrice().compareTo(balance) == 1) {
            auditDao.writeAuditEntry("Insufficient Funds For Purchase Exception:\n\t\t\t\t "
                    + "The user attempted to purchase " + item.getName() +  
                    ".\n\t\t\t\t However they only have £" + balance.toString() + 
                    " and cannot afford anything in the vending machine\n");  
             throw new InsufficientFundsForPurchaseException("Insufficient funds to buy anything.");
        }
        
        //check to ensure that item is in stock
        if (dao.getItem(itemNumber).getQuantityInStock() == 0) {
            auditDao.writeAuditEntry("NoItemInventoryException:\n\t\t\t\t The user tried to select " 
                    + item.getName() + ",\n\t\t\t\t But there are no stocks remaining of this item.");
            throw new NoItemInventoryException("Sorry :( - " + item.getName() + " is out of stock!");
        }
        

        //check to ensure that the user has enough money for the requested item
        if (balance.compareTo(item.getCost()) == -1 ) {
            auditDao.writeAuditEntry("Insufficient Funds Exception:\n\t\t\t\t User attempted to buy " + item.getName() + 
                    "\n\t\t\t\t But they only had a balance of £" + balance.toString() + " and the item costs £" + item.getCost().toString());
            throw new InsufficientFundsException("Insufficient Funds. \n"+ "You don't have enough money for " + 
                item.getName() + "Your balance: £" + balance.toString() + "\nCost of " + item.getName() 
                + " is £" + item.getCost().toString());
        }
        
        updateInventory(item);
        
        //calculate change from remaining balance after purchase
        Change changeDue = new Change(balance.subtract(item.getCost()));
        
        //write to audit file
        auditDao.writeAuditEntry("User successfully bought " + item.getName() + " for £" + item.getCost()
                + "\n\t\t\t\t Change due: £" + balance.subtract(item.getCost()));
        
        //reset balance to zero after purchase
        balance = new BigDecimal(0);
        
        return changeDue;
        
    }
    
    
    @Override
    public void addToBalance(int coinChoice) throws VendingMachinePersistenceException {
       switch (coinChoice) {
           case 1 -> { 
               balance = balance.add(Coins.FIVE_PENCE.getValue());
               auditDao.writeAuditEntry("User inserted  5 pence.     Balance updated to £" + balance.toString() );
            }
           case 2 -> {
               balance = balance.add(Coins.TEN_PENCE.getValue());
               auditDao.writeAuditEntry("User inserted 10 pence.     Balance updated to £" + balance.toString() );
            }
           case 3 -> {
               balance = balance.add(Coins.TWENTY_PENCE.getValue());
               auditDao.writeAuditEntry("User inserted 20 pence.     Balance updated to £" + balance.toString() );
            }
           case 4 -> {
               balance = balance.add(Coins.FIFTY_PENCE.getValue());
               auditDao.writeAuditEntry("User inserted 50 pence.     Balance updated to £" + balance.toString() );
            }
           case 5 -> { 
               balance = balance.add(Coins.ONE_POUND.getValue());
               auditDao.writeAuditEntry("User inserted  1 pound.     Balance updated to £" + balance.toString() );
            }
           case 6 -> {
               balance = balance.add(Coins.TWO_POUNDS.getValue()); 
               auditDao.writeAuditEntry("User inserted  2 pounds.    Balance updated to £" + balance.toString() );
            }
       } 
    }
    
    @Override
    public BigDecimal getBalance() {
        return balance.setScale(2);
    }
    
    
    //method calls to print in audit for starting and ending audit receipts
    public void startAudit() throws VendingMachinePersistenceException {
        auditDao.writeAuditEntry("====================== START OF TRANSACTION ======================\n");
    }
    
    public void exitAudit(String exitMessage) throws VendingMachinePersistenceException {
        auditDao.writeAuditEntry(exitMessage);
        auditDao.writeAuditEntry("====================== END OF TRANSACTION ======================\n\n\n");
    }


}
