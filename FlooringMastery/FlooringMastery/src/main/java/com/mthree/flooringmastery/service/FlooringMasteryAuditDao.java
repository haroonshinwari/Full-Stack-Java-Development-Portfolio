/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.Order;

/**
 *
 * @author Haroon
 */
public interface FlooringMasteryAuditDao {
    
    public void writeAuditEntry(String entry) throws FlooringMasteryPersistenceException;
    
    public void writeAuditOrderEntry(Order order) throws FlooringMasteryPersistenceException;
    
}
