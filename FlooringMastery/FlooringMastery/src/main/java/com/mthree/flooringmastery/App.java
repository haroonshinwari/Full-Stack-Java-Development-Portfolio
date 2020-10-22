/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.flooringmastery;

import com.mthree.flooringmastery.controller.FlooringMasteryController;
import java.util.NoSuchElementException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Haroon
 */
public class App {
    
    public static void main(String[] args) {
        
         //>>>>>>> BEFORE SPRING DI <<<<<<<<
//        //View
//        UserIO myIO = new UserIOConsoleImpl();
//        FlooringMasteryView view = new FlooringMasteryView(myIO);
//
//        //Service Layer + DAOs
//        ProductDao productDao = new ProductDaoFileImpl();
//        TaxDao taxDao = new TaxDaoFileImpl();
//        OrdersDao ordersDao = new OrdersDaoFileImpl(productDao, taxDao);
//        FlooringMasteryAuditDao auditDao = new FlooringMasteryAuditDaoImpl();
//        FlooringMasteryServiceLayer service = new FlooringMasteryServiceLayerImpl(ordersDao, productDao, taxDao, auditDao);
//
//        //Controller
//        FlooringMasteryController controller = new FlooringMasteryController(service, view);
//        controller.run();


        Thread shutdownHook = new Thread("myapp-shutdown-hook") {
        @Override
        public void run() {
                System.out.println("Starting FlooringMastery shutdown...");
                // Do some cleanup work.
                System.out.println("FlooringMastery shutdown complete.");
            }
        };
        
        try {
            Runtime.getRuntime().addShutdownHook( shutdownHook );

            ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
            FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);
            controller.run();
        
        }catch (NoSuchElementException e) {     //for Ctrl + C in Windows command line
            System.out.println("Time for a graceful exit. Farewell :)");
        }
    }       
}
