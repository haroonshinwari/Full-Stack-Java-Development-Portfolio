/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary.ui;

import com.mthree.dvdlibrary.dao.DVDLibraryDaoException;
import com.mthree.dvdlibrary.dto.DVD;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


/**
 *
 * @author Haroon
 */
public class DVDLibraryView {
    
    final private UserIO io;
    final private DVD exitDVD = new DVD("exit");
    
    public DVDLibraryView(UserIO io) {
        this.io =io;
    }
    
    public int printMenuAndGetSelection() {
        io.print("++++++++++++++++++++++ \n     Main Menu\n++++++++++++++++++++++ ");
        io.print("1.  List all the DVDs in the library");
        io.print("2.  Add a new DVD to the library");
        io.print("3.  View a DVD from the library");
        io.print("4.  Remove DVD information by title");
        io.print("5.  Edit a DVD in the library");
        io.print("\n6.  Find all movies released in the last N years");
        io.print("7.  Find all DVDs with a given MPAA Rating");
        io.print("8.  Find the average age of the movies in the collection");
        io.print("9.  Display the oldest dvd in the library");
        io.print("10. Display the newest dvd in the library");
        io.print("\n0. Exit");
        
        return io.readInt("Please select from the above choices." , 0, 10);
    }
    
    public void displayAddDVDBanner() {
        io.print("=== Add DVD ===");
    }
    
    public DVD getNewDVDInfo() throws DVDLibraryDaoException {
        io.print("You may enter \"exit\" at any time to cancel adding a new DVD and return to the main menu.");
        LocalDate exitDate = LocalDate.of(1000, 01, 01);
        
        
        String title = io.readNonEmptyString("Please enter the DVD title you would like to add: ");
        DVD currentDVD = new DVD(title);
        if (title.equalsIgnoreCase("exit")) {return exitDVD;}
        
        LocalDate releaseDate = getReleaseDate(title);
        if (releaseDate.equals(exitDate)) { return exitDVD;}
        
        String mPAARating = getUserMPAARating();
        if (mPAARating.equalsIgnoreCase("exit")) { return exitDVD; }
        
        io.print("+++++++++++++++++ Directors Name +++++++++++++++++");
        String directorsName = io.readNonEmptyString("Please enter the Director's name: ");
        if (directorsName.equalsIgnoreCase("exit")) {return exitDVD; }
        
        io.print("+++++++++++++++++ Studio +++++++++++++++++");
        String studio = io.readNonEmptyString("Please enter the studio: ");
        if (studio.equalsIgnoreCase("exit")) {return exitDVD;}
        
        io.print("+++++++++++++++++ User Rating and Review +++++++++++++++++");
        String userReview = getRatingAndReview();
        if (userReview.contains("exit")) {return exitDVD;}

        currentDVD.setReleaseDate(releaseDate);
        currentDVD.setMPAARating(mPAARating);
        currentDVD.setDirectorsName(directorsName);
        currentDVD.setStudio(studio);
        currentDVD.setUserReview(userReview);
        
        
        return currentDVD;   
        }       
    
    public void displayCreateSuccessBanner() {
        io.readString("DVD successfully created. Please hit enter to continue");
    }
    
    
    public void displayDisplayAllBanner() {
        io.print("=== Display all DVDs ===");
    }
    
    public void displayDVDList(List<DVD> dvdList) {
        io.print("There are " + dvdList.size() + " DVDs in the collection.\n");
        for(DVD currentDVD: dvdList) {
            String dvdInfo = String.format("    %s [%s] Director: %s", 
                    currentDVD.getTitle(),
                    currentDVD.getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM, uuuu")),  //date format 24 January, 1994
                    currentDVD.getDirectorsName());
            io.print(dvdInfo);
            
        }
        io.readString("\nPlease hit enter to continue.");
    }  
    
    public void displayDVDBanner() {
        io.print("=== Display DVD ===");
    }
    
    public String getDVDTitleChoice() {
        return io.readNonEmptyString("Please enter the DVD title: ");
    }
    
    public void displayDVD(DVD dvd, boolean hitEnter) {
        if (dvd != null) {
            io.print("Title: " + dvd.getTitle());
            io.print("Release Date: " + dvd.getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM, uuuu")));
            io.print("MPAA Rating: " + dvd.getMPAARating());
            io.print("Director's Name: " + dvd.getDirectorsName());
            io.print("Studio: " + dvd.getStudio());
            
            io.print("User Review: " + dvd.getUserReview());
            io.print("");     
        }else {
            io.print("No such DVD");
        }
        if (hitEnter) {
            io.readString("Please hit enter to continue.");
        }
    }
    
    public void displayRemoveDVDBanner() {
        io.print("=== Remove DVD ===");
    }
    
    public void displayRemoveResults(DVD dvdRecord) {
        if (dvdRecord != null) {
            io.print("DVD successfully removed from the library");
        }else {
            io.print("No such DVD");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayEditDVDBanner() {
        io.print("=== Edit DVD ===");
    }
    
    public DVD getEditDVDInformation(DVD dvd) throws DVDLibraryDaoException {
        
        if (dvd != null) {
            io.print("What attribute of the DVD would you like to edit? ");
            io.print("1. Release Date\n2. MPAA Rating\n3. Director's Name\n4. Studio\n5. User Review\n6. Go back to the Main Menu");
            
            int editChoice = io.readInt("Please select from the above choices." , 1, 6 );
            
            switch (editChoice) {
                case 1: 
                    LocalDate releaseDate = getReleaseDate(dvd.getTitle());
                    dvd.setReleaseDate(releaseDate);
                    break;
                case 2: 
                    io.print("1. G - General Audience\n2. PG - Parental Guidance Suggested\n"
                        + "3. PG-13 - Parents Strongly Cautioned\n4. R - Restricted\n5. NC-17 - Adults Only ");
                    int intMPAARating = io.readInt("Please select a MPAA rating from the above choices: ", 1, 5);
                    String stringMPAARating = mPAARatingIntToString(intMPAARating);
                    dvd.setMPAARating(stringMPAARating);
                    break;
                case 3: 
                    String directorsName = io.readNonEmptyString("Please enter the Director's name: ");
                    dvd.setDirectorsName(directorsName);
                    break;
                case 4: 
                    String studio = io.readNonEmptyString("Please enter the studio: ");
                    dvd.setStudio(studio);
                    break; 
                case 5: 
                    String completeReview = getRatingAndReview();
                    dvd.setUserReview(completeReview);
                    break;      
            }
        }else {
            System.out.println("Invalid input");
        }
        return dvd;
    }
    
    
    
    public void displayEditResults(DVD dvdRecord) {
        if (dvdRecord != null) {
            io.print("DVD successfully edited from the library");
        }else {
            io.print("No such DVD");
        }
        io.readString("Please hit enter to continue.");
    }
    
    
    public void displayExitBanner() {
        io.print("Good Bye!!");
    }
    
    public void displayUnkownCommandBanner() {
        io.print("Unknown Command!!!");
    }
    
    public void displayErrorMessage(String errorMessage) {
        io.print("=== ERROR ===");
        io.print(errorMessage);
    }
    
    public String mPAARatingIntToString(int userIntChoice) {
        String mPAARating = " ";
        switch (userIntChoice) {
            case 0:
                mPAARating = "exit";
                break;
            case 1: 
                mPAARating = "G";
                break;
            case 2: 
                mPAARating = "PG";
                break;
            case 3: 
                mPAARating = "PG-13";
                break;
            case 4: 
                mPAARating = "R";
                break;
            case 5: 
                mPAARating = "NC-17";
                break;
        }
        return mPAARating;
    }
    
    public LocalDate getReleaseDate(String title) throws DVDLibraryDaoException {
        io.print("+++++++++++++++++ Release Date +++++++++++++++++");
        String date;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu MMM dd");
        LocalDate firstFilm = LocalDate.of(1888, 01, 01);
        LocalDate today = LocalDate.now();
        boolean keepGoing = true;
        LocalDate releaseDate = LocalDate.of(1000, 1, 1);
        
        while (keepGoing) {
            try {
                date = io.readNonEmptyString("Please enter the date in the format yyyy-mm-dd");
                if (date.equalsIgnoreCase("exit")) {
                    return releaseDate;
                }
                releaseDate = LocalDate.parse(date);
                    if (releaseDate.isBefore(firstFilm)) {
                        io.print("Please enter a date after 01-01-1888");
                    } else if (releaseDate.isAfter(today)) {
                        io.print("Please enter a date before " + today.toString());
                    } else {
                        keepGoing = false;
                    } 
                

            }catch(Exception e) {
                System.out.println("Invalid Date entered." + e);
            }
        }
        return releaseDate;
    }
    
    public String getRatingAndReview() {
        String userRating = io.readNonEmptyString("Please enter your rating out of 10 : ");
        String completeReview  = " ";
        
        if (userRating.equalsIgnoreCase("exit")) {
            completeReview = "exit";
        }else {
            try {
                Double userRatingDouble = Double.parseDouble(userRating);
                if (userRatingDouble < 0 || userRatingDouble > 10 ) {
                    io.print("Please enter a valid rating between 0 and 10");
                    getRatingAndReview();
                }else {
                    String userReview = io.readString("Please enter your review: (or skip by pressing enter)"
                        + "\nYou can still cancel by entering \"exit\". ");
                    if (userReview.equalsIgnoreCase("exit")) {
                        completeReview = "exit";
                    }else {
                        completeReview = "[" + userRating + "/10]: " + userReview;
                    }
                }
            }catch(NumberFormatException e) {
                io.print("Please enter a number between 0 and 10 and not letters");
                getRatingAndReview();
            }
        }
        return completeReview;
    }
    
    
    public void displayCancelMessage() {
        io.readString("The new DVD will not be added. Please hit enter to continue");
    }
    
    
    public String getUserMPAARating() {
        io.print("+++++++++++++++++ MPAA Rating +++++++++++++++++");
        io.print("1. G - General Audience\n2. PG - Parental Guidance Suggested\n"
            + "3. PG-13 - Parents Strongly Cautioned\n4. R - Restricted\n5. NC-17 - Adults Only\n\n0. Exit ");
        
        int userMPAARating = io.readInt("Please select a MPAA rating from the above choices: ", 0, 5);
        String mPAARating = mPAARatingIntToString(userMPAARating);
        
        return mPAARating;
    }


    public String displayDuplicate(DVD newDVD, DVD duplicateDVD) {
        io.print("******************");
        displayDVD(newDVD, false);
        io.print("******************");
        displayDVD(duplicateDVD, false);
        io.print("******************");
        io.print("There is another DVD with the same title in the library already");
        String duplicateAnswer = io.readString("Would you like to overwrite the old copy? "
                + "\nPlease enter \"yes\" if you would like to overwrite, or enter to cancel");
        return duplicateAnswer;  
    }

    public void displayFindDVDReleasedinLastNYearsBanner() {
        io.print("=== Display all DVDs Released in the Last N years ===");
        
    }

    public int getNYears() {
        int nYear = io.readInt("Please enter the year from which you would like to see the list of DVDs released after:");
        return nYear;
    }
    
    public void displayDVDByMPAARatingBanner() {
        io.print("=== Display all DVDs with given MPAA Rating ===");
        
    }

    public void displayAverageAgeOfCollectionBanner() {
        io.print("=== Display the average age of the DVD collection ===");
    }

    public void displayAverageAgeOfCollection(double averageAge) {
        io.print("\nThe average of the DVD collection is " + averageAge + " years");  
        
        io.readString("Please hit enter to continue");
        
    }
    
    public void displayOldestDVDInCollectionBanner() {
        io.print("=== Display the oldest DVD in the collection ===");
    }
    
    
    public void displayNewestDVDInCollectionBanner() {
        io.print("=== Display the newest DVD in the collection ===");
    }
}
