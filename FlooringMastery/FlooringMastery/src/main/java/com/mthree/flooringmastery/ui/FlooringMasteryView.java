/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.ui;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class FlooringMasteryView {
    
    final private UserIO io;
    
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection(){
        io.print("\n\n* * * * * * * * * * * * * * * * * * *");
        io.print("*  <<Flooring Program>>             *\n"
                +"*                                   *");
        io.print("* 1. Display Orders                 *");              
        io.print("* 2. Add an Order                   *");
        io.print("* 3. Edit an Order                  *");
        io.print("* 4. Remove an Order                *");
        io.print("* 5. Update Status of Order         *");
        io.print("* 6. Export All Data                *\n"
                +"*                                   *");
        io.print("* 0. Exit program                   *");
        io.print("* * * * * * * * * * * * * * * * * * *\n");
        
        io.print("+ - + - + - + - + - + - + - + - + - + - + - + - + - + - + - + -");
        io.print("- You may enter \"main\" at anytime to return to the main menu  -");
        io.print("+ You may enter \"exit\" at anytime to exit the program         +");
        io.print("- + - + - + - + - + - + - + - + - + - + - + - + - + - + - + - +\n");
        try {
            return io.readInt("Please select from the above choices.", 0, 7);
        }catch (MainMenuException e) {
            return 7;
        }catch (ExitException ex) {
            return 0;
        }
    }
    
    //*****************************************************
    // >>>> >>>> DISPLAY BANNER MESSAGES TO USER <<<< <<<<  
    //*****************************************************
    public void displayOrdersBanner() {
        io.print("============== *** Display Orders *** ==============");
    }
    public void displayAddOrderBanner() {
        io.print("============== *** Add an Order *** ==============\n");
    }
    public void displayEditOrderBanner() {
        io.print("============== *** Edit an Order *** ==============");
    }
    public void displayRemoveOrderBanner() {
        io.print("============== *** Remove Order *** ==============");
    }
    public void displayUpdateStatusBanner() {
        io.print("============== *** Update Status *** ==============");
    }
    public void displayExportDataBanner() {
        io.print("============== *** Export All Data *** ==============");
    }
    public void displayUnkownCommandBanner() {
        io.print("Uknown Command. Please enter a value between 0 and 6 ");
    }

    //*************************************************
    // >>>> >>>> DISPLAY INFORMATION TO USER <<<< <<<<
    //*************************************************
    public LocalDate askUserForDeliveryDate(String stringPrompt)throws MainMenuException, ExitException {
        return io.readLocalDate(stringPrompt);
    }

    public int askUserForOrderNumber(String stringPrompt)throws MainMenuException, ExitException {
        return io.readInt(stringPrompt);
    }

    public void displayErrorMessage(String string) {
        io.print(string);
    }
    
    public void displayOrder(Order order) {
        
        io.print("Delivery Date           " + order.getDeliveryDate().format(DateTimeFormatter.ofPattern("E, d MMM u")));
        io.print("Order Number:           " + order.getOrderNumber());
        io.print("Customer Name:          " + order.getCustomerName());
        io.print("State:                  " + order.getStateTax().getStateAbbreviation() + ", " + order.getStateTax().getStateName());
        io.print("Tax Rate:               " + order.getStateTax().getTaxRate().setScale(2) + "%");
        io.print("Product:                " + order.getProduct().getProductType());
        io.print("Area:                   " + order.getArea().setScale(2, RoundingMode.HALF_UP));
        io.print("Cost per sq.f:          £" + order.getProduct().getCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP));
        io.print("Labour Cost per sq.f:   £" + order.getProduct().getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP));
        io.print("Material Cost:          £" + order.getMaterialCost().setScale(2, RoundingMode.HALF_UP));
        io.print("Labour Cost:            £" + order.getLaborCosts().setScale(2, RoundingMode.HALF_UP));
        io.print("Tax:                    £" + order.getTax().setScale(2, RoundingMode.HALF_UP));
        io.print("Total:                  £" + order.getTotal().setScale(2, RoundingMode.HALF_UP));
        io.print("Order Date:             " + order.getOrderDate().format(DateTimeFormatter.ofPattern("E, d MMM u")));
        io.print("Last Updated:           " + order.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("E, d MMM u")));
        io.print("Orer Status             " + order.getOrderStatus().toString());
        io.print("---------------------------------------------------------------");
    }
    
    public void displayEditedOrder(Order oldOrder, Order newOrder) {
        io.print("Delivery Date           " + oldOrder.getDeliveryDate().format(DateTimeFormatter.ofPattern("E, d MMM u")));
        io.print("Order Number:           " + newOrder.getOrderNumber() + "[" +  oldOrder.getOrderNumber() + "]");
        io.print("Customer Name:          " + newOrder.getCustomerName() + "[" +  oldOrder.getCustomerName() + "]");
        io.print("State:                  " + newOrder.getStateTax().getStateAbbreviation() + ", " + oldOrder.getStateTax().getStateName() + "[" + newOrder.getStateTax().getStateAbbreviation() + ", " + oldOrder.getStateTax().getStateName() + "]");
        io.print("Tax Rate:               " + newOrder.getStateTax().getTaxRate().setScale(2) + "%" + "[" + oldOrder.getStateTax().getTaxRate().setScale(2) + "]");
        io.print("Product:                " + newOrder.getProduct().getProductType() +"[" + oldOrder.getProduct().getProductType() + "]");
        io.print("Area:                   " + newOrder.getArea().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getArea().setScale(2, RoundingMode.HALF_UP) + "%" + "]\n");

        io.print("Cost per sq.f:          £" + newOrder.getProduct().getCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getProduct().getCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP) + "]");
        io.print("Labour Cost per sq.f:   £" + newOrder.getProduct().getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getProduct().getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP) + "]");
        io.print("Material Cost:          £" + newOrder.getMaterialCost().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getMaterialCost().setScale(2, RoundingMode.HALF_UP) + "]");
        io.print("Labour Cost:            £" + newOrder.getLaborCosts().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getLaborCosts().setScale(2, RoundingMode.HALF_UP)  + "]");
        io.print("Tax:                    £" + newOrder.getTax().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getTax().setScale(2, RoundingMode.HALF_UP)  + "]");
        io.print("Total:                  £" + newOrder.getTotal().setScale(2, RoundingMode.HALF_UP) + "[" + oldOrder.getTotal().setScale(2, RoundingMode.HALF_UP) + "]\n");
        
        io.print("Order Date:             " + newOrder.getOrderDate().format(DateTimeFormatter.ofPattern("E, d MMM u")));
        io.print("Last Updated:           " + oldOrder.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("E, d MMM u")) + "[" + newOrder.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("E, d MMM u")) + "]" );
        io.print("Order Status            " + oldOrder.getOrderStatus().toString());
        io.print("---------------------------------------------------------------");
    }

    public void displayOrders(List<Order> ordersList) {
        ordersList.forEach(order -> {
            displayOrder(order);
        });
    }
    
    public void displayStates(List<StateTax> stateTaxList) {
        stateTaxList.forEach(stateTaxObject -> {
            io.print(stateTaxObject.getStateAbbreviation() + ", " + stateTaxObject.getStateName());
        });
    } 
    
    public void displayProducts(List<Product> products) {
        io.print("-*-*-*-*-*-*- PRODUCTS -*-*-*-*-*-*-\n");
        for (Product product: products) {
            io.print("Product: " + product.getProductType());
            io.print("Cost per square foot: £" + product.getCostPerSquareFoot().setScale(2).toString());
            io.print("Labour Cost per square foot: £" + product.getLaborCostPerSquareFoot().setScale(2).toString());
            io.print("-----------------------------------------");
        }
    } 
    
    public void displayOrderStatusList () {
        
        io.print("1. " + OrderStatus.APPENDING.toString() + "\n" +
                 "2. " + OrderStatus.PAID.toString() + "\n" +
                 "3. " + OrderStatus.IN_FULFILMENT.toString() + "\n" +
                 "4. " + OrderStatus.DELIVERED.toString() + "\n" +
                 "5. " + OrderStatus.CLOSED.toString() + "\n");
    }
    

    //***************************************************
    // >>>> >>>> DISPLAY/REQUEST INPUT FROM USER <<<< <<<<
    //***************************************************
    public int askUserMainOrExit() {
        io.print("\n1. Main Menu");
        io.print("0. Exit");
        int intMainOrExit = 5;
        try {
            intMainOrExit = io.readInt("Please enter 1 to return to the main menu or 0 to exit", 0, 1);
        }catch (MainMenuException e){
            return 1;
        }catch (ExitException ex) {
            return 0;
        }
        return intMainOrExit;
    }
    
    //only for when adding an order
    public LocalDate askUserForFutureDate(String stringPrompt) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- DELIVERY DATE -*-*-*-*-*-*-\n");
        LocalDate userDate = io.readLocalDate(stringPrompt);
        if (userDate.isBefore(LocalDate.now())) {
            askUserForFutureDate(stringPrompt + "\nPlease enter a date in the future" );
        }
        return userDate;
    }
    
    public String askUserForCustomerName(String stringPrompt) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- CUSTOMER NAME -*-*-*-*-*-*-\n");
        String name = io.readString(stringPrompt);
       
        if(name.matches("^(\\w)+([\\w\\s\\.,-])*")) { //ensures user must enter 0-9 [a-z] (,.-)accepts ,.-
            io.print("Matches. Valid customer name entered");
        }else {
            askUserForCustomerName(stringPrompt + "\nDoesn't Match. Invalid Input. Please try again. Ensure that you fit the criteria for company name");
        }
        return name;
    }
    
    public String askUserForCustomerName(String stringPrompt, String previousName) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- CUSTOMER NAME -*-*-*-*-*-*-\n");
        String name = io.readString(stringPrompt);

        if (name.isEmpty() || name.isBlank()) {
            return previousName;
        }
        if (name.matches("^(\\w)+([\\w\\s\\.,-])*")) { //ensures user must enter 0-9 [a-z] (,.-)accepts ,.-
            io.print("Matches. Valid customer name entered");
        } else {
            askUserForCustomerName(stringPrompt + "\nDoesn't Match. Invalid Input. Please try again. "
                    + "Ensure that you fit the criteria for company name", previousName);
        }
        return name;
    }

    public StateTax askUserForState(String stringPrompt, List<StateTax> stateTaxList) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- STATE -*-*-*-*-*-*-\n");
        boolean stateKeepGoing = true;
        String userGivenstateAbbreviation = " ";
        StateTax stateTaxChosen = new StateTax(userGivenstateAbbreviation);
        
        displayStates(stateTaxList);
        do {   
            userGivenstateAbbreviation = io.readNonEmptyString("Here is a list of the states above. Please enter the state abbreviation");
            
            for (StateTax stateTaxObject : stateTaxList) {
                
                if (userGivenstateAbbreviation.equalsIgnoreCase(stateTaxObject.getStateAbbreviation())) {
                    stateTaxChosen = stateTaxObject;
                    stateKeepGoing = false;
                }
            }
        }while(stateKeepGoing);
        return stateTaxChosen;
    }
    
    //overloaded edit version
    public StateTax askUserForState(String stringPrompt, List<StateTax> stateTaxList, StateTax previousStateTax) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- STATE -*-*-*-*-*-*-\n");
        boolean stateKeepGoing = true;
        String userGivenstateAbbreviation = " ";
        StateTax stateTaxChosen = new StateTax(userGivenstateAbbreviation);
        displayStates(stateTaxList);
        do {   
            
            userGivenstateAbbreviation = io.readString("Here is a list of the states above\nPlease enter the state abbreviation");
            if (userGivenstateAbbreviation.isEmpty() || userGivenstateAbbreviation.isBlank()) {
                return previousStateTax;
            }
            
            for (StateTax stateTaxObject : stateTaxList) {
                if (userGivenstateAbbreviation.equalsIgnoreCase(stateTaxObject.getStateAbbreviation())) {
                    stateTaxChosen = stateTaxObject;
                    stateKeepGoing = false;
                }
            }
        }while(stateKeepGoing);
        return stateTaxChosen;
    }

    public Product askUserForProductType(String stringPrompt, List<Product> listOfProducts) throws MainMenuException, ExitException {
        boolean productTypeKeepGoing = true;
        String userGivenProductType = " ";
        Product productChosen = new Product(userGivenProductType);
        displayProducts(listOfProducts);
        do {
            userGivenProductType = io.readString(stringPrompt);
            for (Product product : listOfProducts) {
                if (userGivenProductType.equalsIgnoreCase(product.getProductType())) {
                    productChosen = product;
                    productTypeKeepGoing = false;
                }
            }
        }while(productTypeKeepGoing);
        
        return productChosen;
    
    }
    
    //overloaded edit version
    public Product askUserForProductType(String stringPrompt, List<Product> listOfProducts, Product previousProduct) throws MainMenuException, ExitException {
        boolean productTypeKeepGoing = true;
        String userGivenProductType = " ";
        Product productChosen = new Product(userGivenProductType);
        displayProducts(listOfProducts);
        do {
            userGivenProductType = io.readString(stringPrompt);
            if (userGivenProductType.isEmpty() || userGivenProductType.isBlank()) {
                return previousProduct;
            }
            for (Product product : listOfProducts) {
                if (userGivenProductType.equalsIgnoreCase(product.getProductType())) {
                    productChosen = product;
                    productTypeKeepGoing = false;
                }
            }
        }while(productTypeKeepGoing);
        
        return productChosen;
    
    }
    
    public BigDecimal askUserForArea(String stringPrompt) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- AREA -*-*-*-*-*-*-\n");
        BigDecimal area = io.readBigDecimal(stringPrompt);
        
        if (area.compareTo(new BigDecimal("100")) == -1) {
            askUserForArea(stringPrompt + "\nPlease enter a value greater than 100");
        }
        return area;
    }
    
    //overloaded edit version
    public BigDecimal askUserForArea(String stringPrompt, BigDecimal previousArea) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- AREA -*-*-*-*-*-*-\n");
        
        String areaString = io.readString(stringPrompt);
        if (areaString.isEmpty() || areaString.isBlank()) {
            return previousArea;
        } else if (areaString.equalsIgnoreCase("exit")) {
            throw new ExitException("User exits from ask area stage");
        }
        BigDecimal area = io.readBigDecimal(areaString);
        
        if (area.toString().isEmpty() || area.toString().isBlank()) {
            return previousArea;
        }
        
        if (area.compareTo(new BigDecimal("100")) == -1) {
            askUserForArea(stringPrompt + "\nPlease enter a value greater than 100");
        }
        return area;
    }
    
    public Order getUserOrderInfo(List<StateTax> listOfStateTax, List<Product> listOfProducts) throws MainMenuException, ExitException {
        
        LocalDate deliveryDate = askUserForFutureDate("Please enter the delivery date in the format MM-DD-YYYY:");
        String customerName = askUserForCustomerName("Please enter a customer name (Must not be blank)"
                + "\n- May contain [a-z] and [0-9] as well as periods, dash and comma characters"
                + "\n- First character must be a letter or number ");
        
        StateTax stateTax = askUserForState("Please enter the state abbreviation", listOfStateTax);
        Product product = askUserForProductType("Please select one of the products from above by entering the product name:", listOfProducts);
        
        BigDecimal area = askUserForArea("Please enter the area (in square feet) minimum: 100 sq.ft");
        
        try {
            Order newOrder = new Order(deliveryDate,customerName, stateTax, product, area);
            return newOrder;
        } catch (FlooringMasteryPersistenceException ex) {
            io.print("ERROR - could not load some ... file");
        }
        
        return null;
    }
    
    public OrderStatus getUpdatedStatus(Order retrievedOrder) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- UPDATE ORDER STATUS -*-*-*-*-*-*-\n");
        displayOrder(retrievedOrder);
        displayOrderStatusList();
        io.print("The Order Status currently is: " + retrievedOrder.getOrderStatus().toString());
        int orderStatusIntChoice = io.readInt("Please enter the new Order Status from one of the above choices by entering 1 to 5.", 1, 5);
        
        
        OrderStatus newOrderStatus = retrievedOrder.getOrderStatus();
        switch (orderStatusIntChoice) {
            case 1:
                newOrderStatus = OrderStatus.APPENDING;
                break;
            case 2:
                newOrderStatus = OrderStatus.PAID;
                break;
            case 3:
                newOrderStatus = OrderStatus.IN_FULFILMENT;
                break;
            case 4:
                newOrderStatus = OrderStatus.DELIVERED;
                break;
            case 5:
                newOrderStatus = OrderStatus.CLOSED;
                break;
                
        } 
        
        return newOrderStatus;
    }

    public Order getEditOrderInfo(Order retrievedOrder, List<StateTax> listOfStateTax, List<Product> listOfProducts) throws MainMenuException, ExitException {
        //customer name edit
        String customerName = askUserForCustomerName("Enter customer name [" + retrievedOrder.getCustomerName() + "]", retrievedOrder.getCustomerName());
        
        //state tax edit
        String msgPromptStateTaxEdit = "Enter State abbreviations [" + retrievedOrder.getStateTax().getStateAbbreviation()+
                ", " + retrievedOrder.getStateTax().getStateName() + "]";
        
        StateTax stateTax = askUserForState(msgPromptStateTaxEdit, listOfStateTax, retrievedOrder.getStateTax());
        
        //product edit
        String msgPromptProductEdit = "Enter Product type [" + retrievedOrder.getProduct().getProductType() + "]";
        Product product = askUserForProductType(msgPromptProductEdit, listOfProducts, retrievedOrder.getProduct());
        
        //area edit
        String msgPromptAreaEdit = "Enter the area (in square feet) minimum: 100 sq.ft [" + retrievedOrder.getArea().toString()+ "]"
                + "\nYou may enter \"main\" to return to the main menu"
                + "\nYou may enter \"exir\" to return to the main menu";
        BigDecimal area = askUserForArea(msgPromptAreaEdit, retrievedOrder.getArea());
        
        try {
            Order editedOrder = new Order(retrievedOrder.getDeliveryDate(),customerName, stateTax, product, area);
            return editedOrder;
        } catch (FlooringMasteryPersistenceException ex) {
            io.print("ERROR - could not load some ... file");
        }
        return null;
    }
    
    //*****************************************************************
    // >>>> >>>> DISPLAY/ASK USER TO CONFIRM/CANCEL MESSAGES <<<< <<<<  
    //*****************************************************************
    
    public boolean confirmOrder(Order newOrder) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- CONFIRM ORDER -*-*-*-*-*-*-\n");
        displayOrder(newOrder);
        boolean confirmOrder = true;
        if (io.readInt("1. Confirm order\n2. Cancel Order (return to main menu)", 1, 2) == 2) {
            confirmOrder = false;
        }
        return confirmOrder;
    }
    
    public boolean confirmEditedOrder(Order oldOrder, Order newOrder) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- CONFIRM EDIT ORDER -*-*-*-*-*-*-\n");
        boolean confirmOrder = true;
        //if ()
        displayEditedOrder(oldOrder, newOrder);
        if (io.readInt("1. Confirm edit\n2. Cancel edit ", 1, 2) == 2) {
            confirmOrder = false;
        }
        return confirmOrder;
    }
    
    public boolean confirmRemoveOrder(Order orderToBeRemoved) throws MainMenuException, ExitException {
        io.print("-*-*-*-*-*-*- CONFIRM REMOVE ORDER -*-*-*-*-*-*-\n");
        displayOrder(orderToBeRemoved);
        
        boolean confirmOrder = true;
        if (io.readInt("1. Confirm removal\n2. Cancel removal", 1, 2) == 2) {
            confirmOrder = false;
        }
        return confirmOrder;
    }
    
    public boolean confirmRemoveOrder() throws MainMenuException, ExitException{
        io.print("\"-*-*-*-*-*-*- CONFIRM UPDATE ORDER STATUS -*-*-*-*-*-*-\n");
        io.print("Are you sure you would like to export data\n"
                + "This will export all ACTIVE orders to a file called DataExport.txt in the folder Backup\n"
                + "This will overwrite any data previously held in DataExport.txt\n");
        boolean confirmUpdate= true;
        if (io.readInt("1. Confirm export data\n2. Cancel export data", 1, 2) == 2) {
            confirmUpdate = false;
        }
        return confirmUpdate;
    }
    
    public boolean confirmUpdateOrderStatus(Order retrievedOldOrder, OrderStatus updatedOrderStatus) throws MainMenuException, ExitException {
        io.print("\"-*-*-*-*-*-*- CONFIRM UPDATE ORDER STATUS -*-*-*-*-*-*-\n");
        if (updatedOrderStatus == retrievedOldOrder.getOrderStatus()) {
            io.print("You have selected the same update status as previously: [" + updatedOrderStatus.toString() + "]");
            return false;
        }else {
            io.print("Previous Order Status: [" + retrievedOldOrder.getOrderStatus().toString() + "] "
                    + "New Order Status: [" + updatedOrderStatus.toString() + "]");
        }
        
        boolean confirmUpdate= true;
        if (io.readInt("1. Confirm update status\n2. Cancel update status", 1, 2) == 2) {
            confirmUpdate = false;
        }
        return confirmUpdate;
    }

    //*************************************************************
    // >>>> >>>> DISPLAY SUCCESS/CANCEL MESSAGES TO USER <<<< <<<<  
    //*************************************************************
    public void displaySuccessAddOrder() {
        io.print("You have successfully added an order!");
    }
    
    public void displayOrderNotAdded() {
        io.print("The order has NOT been added");
    }
    
    public void displaySuccessEditOrder() {
        io.print("Successfully edited order!");
    }
    
    public void displayOrderNotEdited() {
        io.print("The order has NOT been edited");
    }

    public void displaySuccessRemoveOrder() {
        io.print("Successfully removed order!");
    }

    public void displayOrderNotRemoved() {
        io.print("The order has NOT been removed");
    }
    
    public void displaySuccessUpdateOrder() {
        io.print("The order status has been updated");
    }
    public void displayOrderStatusNotUpdated() {
        io.print("The order status has NOT been updated");
    }
    
    public void displaySuccessExportData() {
        io.print("The data has been exported!");
    }
    
    public void displayDataNotExported() {
        io.print("The data has NOT been exported");
    }
    
    public void displayExitBanner() {
        io.print("* * * * * * Thank you for using the Flooring Mastery Program * * * * * *");
    }

    public void displayNoNeedToUpdateStatus() {
        io.print("Given you have selected the same order status. No need to update order status!");
    }

}
