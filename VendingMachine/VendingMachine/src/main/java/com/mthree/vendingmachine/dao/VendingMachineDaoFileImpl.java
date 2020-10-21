/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.vendingmachine.dao;

import com.mthree.vendingmachine.dto.Item;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Haroon
 */
public class VendingMachineDaoFileImpl implements VendingMachineDao {
    
    public static final String DELIMITER = "::";
    public final String INVENTORY_FILE;
    
    private Map<Integer, Item> itemsHashMap = new HashMap<>();
    
    public VendingMachineDaoFileImpl() {
        INVENTORY_FILE = "Inventory/Inventory.txt";
    }
    
    public VendingMachineDaoFileImpl(String inventoryTextFile) throws VendingMachinePersistenceException {
        INVENTORY_FILE = inventoryTextFile;
        loadInventory();
    }
    
    @Override
    public Item getItem(int itemNumber) throws VendingMachinePersistenceException {
        return itemsHashMap.get(itemNumber);
    }
    
    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException{
        loadInventory();
        return new ArrayList<>(itemsHashMap.values());
        
    }

    @Override
    public List<Item> getAvailableItems() throws VendingMachinePersistenceException{
         loadInventory();
         List<Item> availableItems = itemsHashMap.values().stream()
                 .filter((item) -> item.getQuantityInStock() > 0)
                 .collect(Collectors.toList());
        
        return availableItems;
    }

    @Override
    public Item updateInventory(Item item)throws VendingMachinePersistenceException {
        loadInventory();
        
        if (itemsHashMap.containsValue(item) && itemsHashMap.get(item.getItemNumber()).getQuantityInStock() > 0) {
            int newStockQuantity = item.getQuantityInStock() -1;
            itemsHashMap.get(item.getItemNumber()).setQuantityInStock(newStockQuantity);
            item.setQuantityInStock(newStockQuantity);
            writeInventory();
            return item;
        }else {
            return null;
        }
    }
    
    //--------READ/WRITE TO FILE METHODS -----------
    /**
     * itemAsText is expecting a line read in from inventory.txt
     * For example, it might look like this:
     * 1::£0.85::Fanta Fruit Twist 330ml::5
     * We then split that line on our DELIMITER - which we are using as ::
     * Leaving us with an array of Strings, stored in itemTokens.
     * Which should look like this:
     * 
     * ------------------------------------------
     * |   |      |                         |   |
     * | 1 | 0.85 | Fanta Fruit Twist 330ml | 5 |   
     * |   |      |                         |   |
     * ------------------------------------------
     * [0]   [1]   [2]           [3]      
     * 
     * [0] Item Number
     * [1] Price of item
     * [2] Name of item
     * [3] Stock
     * 
     * @param itemAsText A string line of text from the inventory text file
     * @return Item generated from the string line from the inventory text file
     */
    public Item unmarshallItem(String itemAsText) {
        
        //retrieve the itemNumber in order to generate an item
        String[] itemTokens = itemAsText.split(DELIMITER);
        int itemNumber = Integer.parseInt(itemTokens[0]);
        Item item = new Item(itemNumber); 
        
        //Retrieve the remaining attributes of the item 
        BigDecimal itemPrice = new BigDecimal(itemTokens[1]);
        String itemName = itemTokens[2];
        int itemQuantityInStock = Integer.parseInt(itemTokens[3]);
        
        //set the variables in the new item attained from string array
        item.setCost(itemPrice);
        item.setName(itemName);
        item.setQuantityInStock(itemQuantityInStock);
        
        return item;
    }

    /**
     * This method loads the Inventory.txt file in the Inventory folder.
     * The text file contains the inventory of every item
     * After loading, this method calls the unmarshallItem method to unmarshall
     * line by line and then add each item to the itemsHashMap
     * @throws VendingMachinePersistenceException 
     */
    public void loadInventory() throws VendingMachinePersistenceException{
        Scanner scanner;
        try {
            File file = new File(INVENTORY_FILE);
            scanner = new Scanner(new FileInputStream(file));
        }catch (FileNotFoundException e) {
            throw new VendingMachinePersistenceException(
                "-_- Could not load inventory data into memory.", e);
        }
    
    
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentItem holds the most recent item unmarshalled
        Item currentItem;
        // Go through INVENTORY_FILE line by line, decoding each line into an 
        // Item object by calling the unmarshallItem method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an Item object
            currentItem = unmarshallItem(currentLine);

            // We are going to use the item ID as the map key for our Item object.
            itemsHashMap.put(currentItem.getItemNumber(), currentItem);
        }
        scanner.close();
    }
    
    /**
     * We need to turn an item object into a line of text for our file.
     * For example, we need an in memory object to end up like this:
     * 4::0.90::Coca Cola 330ml:: 8
     * 
     * @param item this is the the item object that will be written to the inventory file
     * @return A string which contains all the item information separated by the delimiter
     */
    public String marshallItem(Item item) {
        
        String itemAsText = Integer.toString(item.getItemNumber()) + DELIMITER;
        itemAsText += item.getCost().toString() + DELIMITER;
        itemAsText += item.getName() + DELIMITER;
        itemAsText += Integer.toString(item.getQuantityInStock());
        
        return itemAsText;
    }

    /**
     * This method writes to the inventory text file by using the marshallItem method
     * This does this by going through each item in the hashmap and then converts using the 
     * marhsallItem method to convert it each item into a single line of text separated by 
     * the delimiter
     */
    public void writeInventory() throws VendingMachinePersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(INVENTORY_FILE));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException(
                    "Could not save inventory data.", e);
        }

        // Write out the DVD objects to the library file.
        String itemAsText;
        List<Item> itemList = new ArrayList(itemsHashMap.values());
        for (Item currentItem : itemList) {
            // turn an item object into a String
            itemAsText = marshallItem(currentItem);
            // write the item object to the file
            out.println(itemAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        out.close();
    }

}
