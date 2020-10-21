/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.ui;

import com.mthree.vendingmachine.dto.Coins;
import com.mthree.vendingmachine.dto.Item;
import com.mthree.vendingmachine.dto.Change;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class VendingMachineView {
    
    final private UserIO io;
    
    public VendingMachineView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection(List<Item> availableItems, BigDecimal balance) {
        io.print("=== Main Menu ===");
        io.print("\n=======================");
        io.print("Vending Machine");
        
        displayInventory(availableItems);
        
        io.print("=======================\n");
        displayBalance(balance);
        io.print("\n1. List inventory ");
        io.print("2. Add Money ");
        io.print("3. Purchase an item");
        io.print("\n0. Exit program");
        
        

        return io.readInt("Please select from the above choices.", 0, 3);
    }
    
    public void displayExitBanner() {
        io.print("Good Bye!!");
    }
    
    public void displayUnkownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayStock(List<Item> items ) {
        for (Item item: items) {
            if (item.getQuantityInStock() == 0) {
                io.print("[OUT OF STOCK]  " + item.getName());
            }else {
                io.print("[ " + item.getQuantityInStock() + " LEFT]       " + item.getName());
            }
        }
    }
    
    public void displayInventory(List<Item> inventory) {
        for (Item item: inventory ) {
            io.print(item.getItemNumber() + ". £" + item.getCost() + " " + item.getName());
        } 
    }
    
    public void displayCurrentBalance(BigDecimal balance) {
        io.print("Your current balance is: £" + balance.setScale(2).toString());
        io.readString("Please press enter to continue and return to the main menu.");
        
    }

    public int getUserInsertCoins() {
        
        int userChoice = io.readInt("Please enter 1 - 6 for  the coin you want to insert into the vending machine."
                + "\nPlease enter 0 to return to the main menu.", 0, 6);
        
        if (userChoice == 0) {
            return 0;
        }else {
            return userChoice;
        } 
    }
    
    public void displayCoinMenu() {
        io.print("1. " + Coins.FIVE_PENCE.toString());
        io.print("2. " + Coins.TEN_PENCE.toString());
        io.print("3. " + Coins.TWENTY_PENCE.toString());
        io.print("4. " + Coins.FIFTY_PENCE.toString());
        io.print("5. " + Coins.ONE_POUND.toString());
        io.print("6. " + Coins.TWO_POUNDS.toString());
        
        io.print("\n0. Return to the main menu\n");
    }

    public void displayBalance(BigDecimal balance) {
        io.print("Your balance: £" + balance.toString());
    }
    
    public void displayChange(Change change) {
        //io.print("Here is your change. Balance restored to £0.00\n");
        io.printChange(change);
    }

    public int askUserForItemChoice(List<Item> listOfAvailableItems, List<Item> listOfAllItems, BigDecimal balance) {
        displayInventory(listOfAvailableItems);
        displayBalance(balance);
        int userBuyChoice = io.readInt("\nPlease enter a choice between 1 and " + (listOfAllItems.size()) + " for the item you want to buy." 
                + "\nPlease enter 0 to go back to the main menu.",0, listOfAllItems.size());
        displayBalance(balance);
        return userBuyChoice;
    }
    
    
    public void displayListInventoryBanner() {
        io.print("============= List Inventory =============");
    }
    
    public void displayViewBalanceBanner() {
        io.print("============= View Balance =============");
    }
    
    public void displayAddCoinsBanner() {
        io.print("============= Insert coins =============");
    }
    
    public void displayBuyItemBanner() {
        io.print("============= Buy an item =============");
    }
    
    public void displaySuccessfulTransactionResults(Item item) {
        io.print("You have succesfully bought " + item.getName() + " for £" + item.getCost());
    }

    public void displayInsufficicentFundsMessage(BigDecimal balance, Item item) {
        io.print("Insufficient Funds. You don't have enough money to buy "+ item.getName()
                + "\nYou have a balance of £" + balance.toString() + " and " + item.getName()
                + " costs £" +item.getCost().toString());
                
        io.readString("Please hit enter to continue.");
    }

    public void displayNoItemInventoryException(Item item) {
        io.print(item.getName() + " is out of stock");
        io.readString("Please hit enter to continue");
    }

    public void displayInsufficicentFundsForPurchaseMessage(BigDecimal balance, Item item) {
        io.print("You selected: " + item.getName());
        io.print("You don't have enough money to purchase anything in the vending machine.\n"
                + "You only have £" + balance.toString());
        io.readString("Please hit enter to continue");
    }

    public void displayNoChangeDue() {
        io.readString("No change due. Enjoy your snack!");
    }

    public int askUserMainOrExit() {
        io.print("\n1. Main Menu");
        io.print("0. Exit");
        int intMainOrExit = io.readInt("Please enter 1 to return to the main menu or 0 to exit", 0, 1);
        return intMainOrExit;
    }
    
    public void displayVendingMachineOutOfStock() {
        io.print("--- VENDING MACHINE OUT OF STOCK ---");
        io.readString("Please press enter to exit the program.");
    }

}
