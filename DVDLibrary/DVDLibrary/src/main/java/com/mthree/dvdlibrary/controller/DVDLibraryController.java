/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary.controller;

import com.mthree.dvdlibrary.dao.DVDLibraryDao;
import com.mthree.dvdlibrary.dao.DVDLibraryDaoException;
import com.mthree.dvdlibrary.dao.DVDLibraryDaoFileImpl;
import com.mthree.dvdlibrary.dto.DVD;
import com.mthree.dvdlibrary.ui.DVDLibraryView;
import com.mthree.dvdlibrary.ui.UserIO;
import com.mthree.dvdlibrary.ui.UserIOConsoleImpl;
import java.util.List;

/**
 *
 * @author Haroon
 */
public class DVDLibraryController {
    
    private DVDLibraryDao dao;
    private DVDLibraryView view;
    
    
    public DVDLibraryController(DVDLibraryDao dao, DVDLibraryView view) {
        this.dao = dao;
        this.view = view;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while(keepGoing) {
            
            menuSelection = getMenuSelection();
            
            switch(menuSelection) {
                case 1:
                    listDVDS();
                    break;
                case 2:
                    createDVD();
                    break;
                case 3:
                    viewDVD();
                    break;
                case 4:
                    removeDVD();
                    break;
                case 5:
                    editDVD();
                    break;
                case 6:
                    findDVDsReleasedInLastNYeats();
                    break;
                case 7:
                    listDVDsByMPAARating();
                    break;
                case 8:
                    findAverageAgeOfCollection();
                    break;
                case 9:
                    displayOldestDVD();
                    break;
                case 10:
                    displayNewestDVD();
                    break;     
                case 0:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
                }
            }
            exitMessage();   
        }catch(DVDLibraryDaoException e) {
            view.displayErrorMessage(e.getMessage());
        }
        
    }
    
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private void createDVD() throws DVDLibraryDaoException {
        view.displayAddDVDBanner();
        DVD newDVD = view.getNewDVDInfo();
        
        if (dao.getDVD(newDVD.getTitle()) != null ){
            DVD duplicateDVD = dao.getDVD(newDVD.getTitle());
            String duplicateAnswer = view.displayDuplicate(newDVD, duplicateDVD);
            if (duplicateAnswer.equalsIgnoreCase("Yes") || duplicateAnswer.equalsIgnoreCase("y")) {
                dao.addDVD(newDVD.getTitle(), newDVD);   
                view.displayCreateSuccessBanner();
            }else {
                view.displayCancelMessage();
            }
        }else if (!newDVD.getTitle().equalsIgnoreCase("exit")) {
            dao.addDVD(newDVD.getTitle(), newDVD);
            view.displayCreateSuccessBanner();  
        }else {
            view.displayCancelMessage();
        }
    }
    
    public void listDVDS() throws DVDLibraryDaoException {
        view.displayDisplayAllBanner();
        List<DVD> dvdList = dao.getAllDVDS();
        view.displayDVDList(dvdList);
    }
    
    private void viewDVD() throws DVDLibraryDaoException {
        view.displayDVDBanner();
        String title = view.getDVDTitleChoice();
        DVD dvd = dao.getDVD(title);
        view.displayDVD(dvd, true);
    }
    
    private void removeDVD() throws DVDLibraryDaoException {
        view.displayRemoveDVDBanner();
        String title = view.getDVDTitleChoice();
        DVD removedDVD = dao.removeDVD(title);
        view.displayRemoveResults(removedDVD);
    }
    
    private void editDVD() throws DVDLibraryDaoException {
        view.displayEditDVDBanner();
        String title = view.getDVDTitleChoice();

        DVD editDVD = dao.getDVD(title);   //returns null or the dvd object
        view.getEditDVDInformation(editDVD);
        dao.editDVD(title, editDVD);
        
        
        view.displayEditResults(editDVD);
    }
    
    private void findDVDsReleasedInLastNYeats() throws DVDLibraryDaoException{
        view.displayFindDVDReleasedinLastNYearsBanner();
        int nYears = view.getNYears();
        List<DVD> listDVD = dao.getAllMoviesInLastNYears(nYears);
        view.displayDVDList(listDVD);
    }
    
    public void listDVDsByMPAARating() throws DVDLibraryDaoException {
        view.displayDVDByMPAARatingBanner();
        String mPAARating = view.getUserMPAARating();
        List<DVD> dvdList = dao.findAllMoviesByMPAARAting(mPAARating);
        view.displayDVDList(dvdList);
    }
    
    public void findAverageAgeOfCollection() throws DVDLibraryDaoException {
        view.displayAverageAgeOfCollectionBanner();
        double averageAge = dao.findAverageAgeOfCollection();
        view.displayAverageAgeOfCollection(averageAge);
    }
    
    public void displayOldestDVD() throws DVDLibraryDaoException{
        view.displayOldestDVDInCollectionBanner();
        DVD oldestDVD = dao.findOldestDVD();
        view.displayDVD(oldestDVD, true);
    }
    
    public void displayNewestDVD() throws DVDLibraryDaoException{
        view.displayNewestDVDInCollectionBanner();
        DVD newestDVD = dao.findNewestDVD();
        view.displayDVD(newestDVD, true);
    }  
    
    public void unknownCommand(){
        view.displayUnkownCommandBanner();
    }
    
    public void exitMessage() {
        view.displayExitBanner();
    }
}
