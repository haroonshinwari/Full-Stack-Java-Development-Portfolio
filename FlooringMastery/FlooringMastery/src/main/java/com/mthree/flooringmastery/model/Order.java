/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.model;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author Haroon
 */
public class Order {
    
    private static final String ORDER_NUMBER_FILE = "Data/OrderNumbers/OrderNumberLog.txt";
    
    int orderNumber;
    LocalDate orderDate;
    LocalDate deliveryDate;
    LocalDate lastUpdatedDate;
    OrderStatus orderStatus;
    
    
    String customerName;
    BigDecimal area;
    
    Product product;
    StateTax stateTax;
    
    BigDecimal materialCost;
    BigDecimal laborCosts;
    BigDecimal tax;
    BigDecimal total;
    
    public Order(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public Order(LocalDate deliveryDate, String customerName, StateTax stateTax, Product product, BigDecimal area) throws FlooringMasteryPersistenceException {
        
        int newOrderNumber = generateOrderNumber();
        
        this.orderNumber = newOrderNumber;
        
        
        this.deliveryDate = deliveryDate;
        this.customerName = customerName;
        this.stateTax = stateTax;
        this.product = product;
        this.area = area;
        
        this.materialCost = area.multiply(product.getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        
        this.laborCosts = area.multiply(product.getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal materialAndLaborCosts = materialCost.add(laborCosts);
        BigDecimal taxRateDivideBy100 = stateTax.getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        
        this.tax = materialAndLaborCosts.multiply(taxRateDivideBy100);

        this.total = (materialAndLaborCosts.add(tax)).setScale(2, RoundingMode.HALF_UP);
        
        this.orderDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
        
        this.setOrderStatus(OrderStatus.APPENDING);
                
    }
    
    public int generateOrderNumber() throws FlooringMasteryPersistenceException {
        
        Scanner scanner;
        try {
            File file = new File(ORDER_NUMBER_FILE);
            scanner = new Scanner(new FileInputStream(file));
        }catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                "-_- Could not load products data into memory.", e);
        }
   
        // Go through ORDER_NUMBER_LOG.txt line by line, decoding each line into a 
        // an int. Keep processing until the most recent order number
        scanner.nextLine();
        int orderNumberInLog = 0;
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            orderNumberInLog = Integer.parseInt(currentLine);
        }
        scanner.close();
        
        //create a new number by adding 1 and write the new number to file
        int generatedOrderNumber = orderNumberInLog + 1;
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ORDER_NUMBER_FILE, true));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not persist audit information.", e);
        }
        out.println(generatedOrderNumber);
        out.flush();
        
        return generatedOrderNumber;
    }
        
    
    //call this method when an order is updated
    public void updateLastUpdated() {
        lastUpdatedDate = LocalDate.now();
    }


    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public StateTax getStateTax() {
        return stateTax;
    }

    public void setStateTax(StateTax stateTax) {
        this.stateTax = stateTax;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCosts() {
        return laborCosts;
    }

    public void setLaborCosts(BigDecimal laborCosts) {
        this.laborCosts = laborCosts;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.orderNumber;
        hash = 17 * hash + Objects.hashCode(this.orderDate);
        hash = 17 * hash + Objects.hashCode(this.deliveryDate);
        hash = 17 * hash + Objects.hashCode(this.orderStatus);
        hash = 17 * hash + Objects.hashCode(this.customerName);
        hash = 17 * hash + Objects.hashCode(this.area);
        hash = 17 * hash + Objects.hashCode(this.product);
        hash = 17 * hash + Objects.hashCode(this.stateTax);
        hash = 17 * hash + Objects.hashCode(this.materialCost);
        hash = 17 * hash + Objects.hashCode(this.laborCosts);
        hash = 17 * hash + Objects.hashCode(this.tax);
        hash = 17 * hash + Objects.hashCode(this.total);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (!Objects.equals(this.deliveryDate, other.deliveryDate)) {
            return false;
        }
        
        if (this.orderStatus != other.orderStatus) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.stateTax, other.stateTax)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCosts, other.laborCosts)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }
    
    
}
