/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.Order;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author Haroon
 */
public class FlooringMasteryAuditDaoImpl implements FlooringMasteryAuditDao {
    
    public static final String AUDIT_FILE = "Audit/audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException {
        PrintWriter out;
       
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not persist audit information.", e);
        }
        
        //String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME.localizedBy(Locale.UK));
        //eee, d MMM u H:m:s z 
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT));
        out.println(timestamp + " : " + entry);
        out.flush();
    }
    
    @Override
    public void writeAuditOrderEntry(Order order) throws FlooringMasteryPersistenceException {
        PrintWriter out;
       
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("Could not persist audit information.", e);
        }
        out.println("Delivery Date:          " + order.getDeliveryDate().format(DateTimeFormatter.ofPattern("d MMM u")));
        out.println("Order Number:           " + order.getOrderNumber());
        out.println("Customer Name:          " + order.getCustomerName());
        out.println("State:                  " + order.getStateTax().getStateAbbreviation() + ", " + order.getStateTax().getStateName());
        out.println("Tax Rate:               " + order.getStateTax().getTaxRate().setScale(2));
        out.println("Product:                " + order.getProduct().getProductType());
        out.println("Area:                   " + order.getArea().setScale(2, RoundingMode.HALF_UP));
        out.println("Cost per sq.f:          £" + order.getProduct().getCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP));
        out.println("Labour Cost per sq.f:   £" + order.getProduct().getLaborCostPerSquareFoot().setScale(2, RoundingMode.HALF_UP));
        out.println("Material Cost:          £" + order.getMaterialCost().setScale(2, RoundingMode.HALF_UP));
        out.println("Labour Cost:            £" + order.getLaborCosts().setScale(2, RoundingMode.HALF_UP));
        out.println("Tax:                    £" + order.getTax().setScale(2, RoundingMode.HALF_UP));
        out.println("Total:                  £" + order.getTotal().setScale(2, RoundingMode.HALF_UP));
        out.println("Order Date:             " + order.getOrderDate().format(DateTimeFormatter.ofPattern("d MMM u")));
        out.println("Last Updated:           " + order.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("d MMM u")));
        out.println("---------------------------------------------------------------");
        out.flush();
    }
    
}
