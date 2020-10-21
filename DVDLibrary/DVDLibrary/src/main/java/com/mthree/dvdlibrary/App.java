/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary;


import com.mthree.dvdlibrary.controller.DVDLibraryController;
import java.util.NoSuchElementException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Haroon
 */
public class App {
    
    public static void main(String[] args) {
        
        Thread shutdownHook = new Thread( "myapp-shutdown-hook" ) {
            @Override
            public void run() {
            System.out.println( "Starting DVD Library shutdown..." );
            // Do some cleanup work.
            System.out.println( "DVD Library shutdown complete." );
            }
        };
        
        try {
            Runtime.getRuntime().addShutdownHook( shutdownHook );
        
        
//            UserIO myIO = new UserIOConsoleImpl();
//            DVDLibraryView myView = new DVDLibraryView(myIO);
//            DVDLibraryDao myDao = new DVDLibraryDaoFileImpl();
//            DVDLibraryController controller = new DVDLibraryController(myDao, myView);
//            controller.run();

            ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
            DVDLibraryController controller = ctx.getBean("controller", DVDLibraryController.class);
            controller.run();

            
        }catch (NoSuchElementException e) {     //for Ctrl + C in Windows command line
            System.out.println("Time for a graceful exit. Farewell :)");
        }
          
    }
}
