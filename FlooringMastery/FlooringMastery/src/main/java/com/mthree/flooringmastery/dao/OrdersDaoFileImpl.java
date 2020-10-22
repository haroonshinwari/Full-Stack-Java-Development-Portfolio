/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.dao;

import java.io.FileInputStream;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.OrderStatus;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.StateTax;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Haroon
 */
public class OrdersDaoFileImpl implements OrdersDao {
    
    //********************************
    // >>>> >>>> FIELDS <<<< <<<<  
    //********************************
    
    private TaxDao taxDao;
    private ProductDao productDao;
    
    private final String ORDERS_FOLDER;
    private final String BACKUP_FOLDER;
    public static final String DELIMITER = "::";
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    DateTimeFormatter formatterFileName = DateTimeFormatter.ofPattern("MMddyyyy");
    
    private static final String HEADER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::"
            + "CostPerSquareFoot::LabourCostPerSquareFoot::MaterialCost::LabourCost::"
            + "Tax::Total::OrderDate::OrderStatus::LastEdited";
    
    
    //***********************************
    // >>>> >>>> CONSTRUCTORS <<<< <<<<  
    //***********************************
    public OrdersDaoFileImpl(ProductDao productDao, TaxDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        ORDERS_FOLDER = "Orders/";
        BACKUP_FOLDER = "Backup/DataExport.txt";
    }
    
    public OrdersDaoFileImpl(ProductDao productDao, TaxDao taxDao, String orderFolder, String backupFolder) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        ORDERS_FOLDER = orderFolder;
        BACKUP_FOLDER = backupFolder;
    }

    //**********************************
    // >>>> >>>> METHODS <<<< <<<<  
    //**********************************
    @Override
    public Order getOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException {
        List<Order> ordersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
        for (Order order : ordersOnDeliveryDate) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    @Override
    public List<Order> getAllOrdersByDeliveryDate(LocalDate deliveryDate) throws FlooringMasteryPersistenceException {
        List<Order> listOfOrders = new ArrayList<>(loadOrders(deliveryDate));
        return listOfOrders;
    }
    

    @Override
    public Order addOrder(LocalDate deliveryDate, Order newOrder) throws FlooringMasteryPersistenceException {
        try {
            List<Order> existingOrdersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
            existingOrdersOnDeliveryDate.add(newOrder);
            writeOrders(deliveryDate, existingOrdersOnDeliveryDate);
            
        }catch(FlooringMasteryPersistenceException e) {
            List<Order> newList = new ArrayList<>();
            newList.add(newOrder);
            writeOrders(deliveryDate, newList);
        }
        return newOrder;
    }

    @Override
    public Order removeOrder(LocalDate deliveryDate, int orderNumber) throws FlooringMasteryPersistenceException {
        List<Order> ordersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
        
        for (Order order : ordersOnDeliveryDate) {
            if (order.getOrderNumber() == orderNumber) {
                ordersOnDeliveryDate.remove(order); 
                writeOrders(deliveryDate, ordersOnDeliveryDate);
                return order;
            }
        }
        return null;
    }

    @Override
    public Order editOrder(LocalDate deliveryDate, int orderNumber, Order newOrder) throws FlooringMasteryPersistenceException {
        List<Order> ordersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
        
        for (Order order : ordersOnDeliveryDate) {
            if (order.getOrderNumber() == orderNumber) {
                ordersOnDeliveryDate.remove(order);
                ordersOnDeliveryDate.add(newOrder);
                writeOrders(deliveryDate, ordersOnDeliveryDate);
                return newOrder;
            }
        }
        return null;
    }

    
    @Override
    public Order updateStatus(LocalDate deliveryDate, int orderNumber, OrderStatus newStatus) throws FlooringMasteryPersistenceException {
        List<Order> ordersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
        for (Order order : ordersOnDeliveryDate) {
            if (order.getOrderNumber() == orderNumber) {
                order.setOrderStatus(newStatus);
                order.setLastUpdatedDate(LocalDate.now());
                writeOrders(deliveryDate, ordersOnDeliveryDate);
                return order;
            }
        }
        return null;
    }
    
    @Override
    public List<Order> exportActiveOrders() throws FlooringMasteryPersistenceException {
        
        //Gather all the files in the orders folder containing orders
        File pathOne = new File(ORDERS_FOLDER);
        File[] allFiles = pathOne.listFiles();
        
        List<String> allOrderFiles = Stream.of(allFiles)
                .filter(file -> !file.isDirectory())
                .filter(file -> file.getName().contains("Orders_"))
                .map(File::getName)
                .collect(Collectors.toList());
        
        //Take a substring of those orders to represent the delivery date as a string
        List<String> stringDateFileNames = allOrderFiles.stream()
                .map(p -> p.substring(7, 15))
                .collect(Collectors.toList());
        
        
        
        List<Order> allActiveOrders = new ArrayList<>();
        for (String dateString: stringDateFileNames) {
            LocalDate deliveryDate = LocalDate.parse(dateString, formatterFileName);
            List<Order> ordersOnDeliveryDate = new ArrayList<>(loadOrders(deliveryDate));
            
            ordersOnDeliveryDate.stream()
                    .filter(order -> (!order.getOrderStatus().equals(OrderStatus.CLOSED)))
                    .forEachOrdered(order -> {
                allActiveOrders.add(order);
            });
        }
        
        writeBackupOrders(allActiveOrders);
        return allActiveOrders;
        
    }
    
    //*****************************************************
    // >>>> >>>> READ/WRITE TO FILE METHODS <<<< <<<<  
    //*****************************************************
    
    /**
     * OrderAsText is expecting a line read in from an order file Orders_MMDDYYYY.txt
     * For example, it might look like this:
     * 1::Ada Lovelace,CA::25.00::Tile::249.00::3.50::4.15::871.50::1033.35::476.21::2381.06::24-01-2021::APPENDING
     * We then split that line on our DELIMITER - which we are using as ::
     * Leaving us with an array of Strings, stored in orderToken.Which should look like this:
 
        --------------------------------------------------------------------------------------------------
        | |            |  |     |    |      |    |    |      |       |      |       |          |         |
        |1|Ada Lovelace|CA|25.00|Tile|249.00|3.50|4.15|871.50|1033.35|476.21|2381.06|01-24-2020|APPENDING|
        | |            |  |     |    |      |    |    |      |       |      |       |          |         |
        --------------------------------------------------------------------------------------------------
        [0]   [1]      [2]  [3]  [4]    [5]   [6]  [7]   [8]    [9]    [10]    [11]    [12]        [13]

        [0]  Order Number - int
        [1]  Customer Name - String
        [2]  State Abbreviation - String
        [3]  Tax Rate - BigDecimal
        [4]  Product Type
        [5]  Area 
        [6]  Cost per SquareFoot
        [7]  Labour Cost per Square Foot
        [8]  Material Cost
        [9]  Labour Cost
        [10] Tax
        [11] Total
        [12] Order Date
        [13] Order Status
     * 
     * 
     * @param orderAsText A string line of text from the inventory text file
     * @param deliveryDateFromFile the delivery date given by the file name
     * @return Order object generated from the string line from an orders text file
     * @throws FlooringMasteryPersistenceException
     */
    public Order unmarshallOrder(String orderAsText, LocalDate deliveryDateFromFile) throws FlooringMasteryPersistenceException {
        
        
        //retrieve the deliveryDate in order to generate an item
        String[] orderToken = orderAsText.split(DELIMITER);
                
        Order order = new Order(deliveryDateFromFile);
        
        
        //set the variables in the new orderattained from string array
        order.setOrderNumber(Integer.parseInt(orderToken[0]));
        order.setCustomerName(orderToken[1]);
        
        //create a new stateTax object based on the information provided
        StateTax stateTax = taxDao.getStateTax(orderToken[2]);
        
        
        stateTax.setStateName(stateTax.getStateName());
        stateTax.setTaxRate(new BigDecimal(orderToken[3]));
        order.setStateTax(stateTax);
        
        //create a new product object based on the information provided
        Product product = new Product(orderToken[4]);
        product.setCostPerSquareFoot(new BigDecimal(orderToken[6]));
        product.setLaborCostPerSquareFoot(new BigDecimal(orderToken[7]));
        order.setProduct(product);
        
        order.setArea(new BigDecimal(orderToken[5]) );
        
        
        //add the remaining variables
        order.setMaterialCost(new BigDecimal(orderToken[8]));
        order.setLaborCosts(new BigDecimal(orderToken[9]));
        order.setTax(new BigDecimal(orderToken[10]));
        order.setTotal(new BigDecimal(orderToken[11]));
        
        order.setOrderDate(LocalDate.parse(orderToken[12], formatter));
        order.setOrderStatus(OrderStatus.valueOf(orderToken[13]));
        order.setLastUpdatedDate(LocalDate.parse(orderToken[14],formatter));
        
        return order;
    }

    
    
    /**
     * This method loads the Order_MMDDYYYY.txt file in the Orders folder
     * The text file contains all orders due for delivery on that date
     * After loading, this method calls the unmarshallItem method to unmarshall
     * line by line and then add each Order object to the itemsHashMap, 
     * with its responding delivery date
     * @param deliveryDate
     * @return 
     * @throws FlooringMasteryPersistenceException - if it cannot open a file 
     */
    public List<Order> loadOrders(LocalDate deliveryDate) throws FlooringMasteryPersistenceException{
        Scanner scanner;
        
        String deliveryDateAsString = deliveryDate.format(formatterFileName);
        String fileName = ORDERS_FOLDER +"Orders_" + deliveryDateAsString + ".txt";
        //File file = new File(fileName);
        
        try {
            scanner = new Scanner(new FileInputStream(fileName));
        }catch (FileNotFoundException e) {
                       throw new FlooringMasteryPersistenceException(
                "-_- Could not load order data into memory.", e);
        }

        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentOrder holds the most recent Order  unmarshalled
        Order currentOrder;
        // Go through Orders_MMddyyyy line by line, decoding each line into an 
        // Order object by calling the unmarshallOrder method.
        // Process while we have more lines in the file
        if (!scanner.hasNextLine()){
            throw new FlooringMasteryPersistenceException("There are no orders in the file to read");
        }
        
        scanner.nextLine();      //export has a problem with this line;
        
        List<Order> orders = new ArrayList<>();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an Order object
            currentOrder = unmarshallOrder(currentLine, deliveryDate);

            // We are going add the current order to a temporary orders list
            orders.add(currentOrder);
        }

        //ordersDaoHashMap.put(deliveryDate, orders);
        scanner.close();
        return orders;
    }
    
        
    
    /**
     * We need to turn an Order object into a line of text for our file.
     * For example, we need an in memory object to end up like this:
     * 1::Ada Lovelace,CA::25.00::Tile::249.00::3.50::4.15::871.50::1033.35::476.21::2381.06::24-01-2021::APPENDING
     * 
     * @param order this is the the Order object that will be written to the Orders folder
     * @return A string which contains all the item information separated by the delimiter
     */
    public String marshallOrder(Order order) {
        
        String orderAsText = Integer.toString(order.getOrderNumber()) + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getStateTax().getStateAbbreviation() + DELIMITER;
        orderAsText += order.getStateTax().getTaxRate().toString() + DELIMITER;
        orderAsText += order.getProduct().getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProduct().getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCosts() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal() + DELIMITER;
        orderAsText += order.getOrderDate().format(formatter) + DELIMITER;
        orderAsText += order.getOrderStatus() + DELIMITER;
        orderAsText += order.getLastUpdatedDate().format(formatter);
        
        return orderAsText;
    }

    /**
     * This method writes to the Orders_MMDDYYYY text file by using the marshallItem method
     * This does this by taking the given DeliveryDate passed  in the parameter and then converts 
     * using the marhsallItem method to convert each order into a single line of text separated by 
     * the delimiter and then take all those orders and put them in one file for each delivery date
     * @param deliveryDate
     * @param orders
     * @throws FlooringMasteryPersistenceException
     */
    public void writeOrders(LocalDate deliveryDate, List<Order> orders) throws FlooringMasteryPersistenceException {
        PrintWriter out;
        
            
        String deliveryDateString = deliveryDate.format(formatterFileName);
        String fileName = ORDERS_FOLDER + "Orders_" + deliveryDateString + ".txt";
        
        try {
            out = new PrintWriter(new FileWriter(fileName));
        }catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                "Could not save order data.", e);
        }


        //first line in text file 
        out.println(HEADER);
        // Write out the Order objects to the Orders folder
        String orderAsText;

        for (Order currentOrder : orders) {
            // turn an Order object into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        out.close();
    }
    
    
    
      /**
       * This includes the delivery date at the end of the order
     * We need to turn an Order object into a line of text for our file.
     * For example, we need an in memory object to end up like this:
     * 1::Ada Lovelace,CA::25.00::Tile::249.00::3.50::4.15::871.50::1033.35::476.21::2381.06::24-01-2021::APPENDING
     * 
     * @param order this is the the Order object that will be written to the Orders folder
     * @return A string which contains all the item information separated by the delimiter
     */
    public String marshallBackupOrder(Order order) {
        
        String orderAsText = Integer.toString(order.getOrderNumber()) + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getStateTax().getStateAbbreviation() + DELIMITER;
        orderAsText += order.getStateTax().getTaxRate().toString() + DELIMITER;
        orderAsText += order.getProduct().getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProduct().getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCosts() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal() + DELIMITER;
        orderAsText += order.getOrderDate().format(formatter) + DELIMITER;
        orderAsText += order.getOrderStatus() + DELIMITER;
        orderAsText += order.getLastUpdatedDate() + DELIMITER;
        orderAsText += order.getDeliveryDate().format(formatter);
        
        return orderAsText;
    }
    
    
    public void writeBackupOrders(List<Order> orders) throws FlooringMasteryPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(BACKUP_FOLDER));
        }catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                "Could not save backup data.", e);
        }

        //first line in text file 
        out.println(HEADER + "::DeliveryDate");

        // Write out the Order objects to the Orders folder
        String orderAsText;
        
        orders.sort(Comparator.comparing(Order::getOrderNumber));
        
        for (Order currentOrder : orders) {
            // turn an Order object into a String
            orderAsText = marshallBackupOrder(currentOrder);
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        out.close();
    }
    
    public boolean checkIfDeliveryFileExists(LocalDate deliveryDate) {
        String date = deliveryDate.format(formatterFileName);
        boolean fileExists = false;
        
        File ordersFile = new File(ORDERS_FOLDER);
        String[] listOfFiles = ordersFile.list();
        
        for (String fileName: listOfFiles) {
            if (fileName.contains(date)) {
                fileExists = true;
            }
        }
        return fileExists;
    }
    
    
    
}  
         