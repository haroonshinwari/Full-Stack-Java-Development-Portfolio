/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery.ui;

/**
 *
 * @author Haroon
 */
public class ExitException extends Exception {

    public ExitException(String message) {
        super(message);
    }
    
    public ExitException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
