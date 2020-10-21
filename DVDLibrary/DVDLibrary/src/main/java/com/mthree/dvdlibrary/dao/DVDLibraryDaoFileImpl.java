/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary.dao;

import com.mthree.dvdlibrary.dto.DVD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Haroon
 */
public class DVDLibraryDaoFileImpl implements DVDLibraryDao{
    
    public static final String LIBRARY_FILE = "data/library.txt";
    public static final String DELIMITER = "::";
    
    private Map<String, DVD> dvdHashMap = new HashMap<>();
            

    @Override
    public DVD addDVD(String title, DVD dvd) throws DVDLibraryDaoException {
        loadLibrary();
        DVD prevDVD = dvdHashMap.put(title, dvd);
        writeLibrary();
        return prevDVD;
    }

    @Override
    public List<DVD> getAllDVDS() throws DVDLibraryDaoException {
        loadLibrary();
        return new ArrayList<DVD>(dvdHashMap.values());
    }

    @Override
    public DVD getDVD (String title) throws DVDLibraryDaoException{
        loadLibrary();
        return dvdHashMap.get(title);
    }

    @Override
    public DVD removeDVD(String title) throws DVDLibraryDaoException {
        loadLibrary();
        DVD removedDVD = dvdHashMap.remove(title);
        writeLibrary();
        return removedDVD;
    }
    
    @Override
    public DVD editDVD(String title, DVD editedDVD) throws DVDLibraryDaoException {
        loadLibrary();
        DVD editDVD = dvdHashMap.put(title, editedDVD);    //return null if empty other a DVD object
        if (editDVD != null) {
            writeLibrary();
        }
        return editedDVD;
    }
    
    private DVD unmarshallDVD(String dvdAsText){
        /* 
         dvdAsText is expecting a line read in from our file.
         For example, it might look like this:
         American Psycho::05/09/2000::R::Mary Harron::Lionsgate::10/10 A Great Film
        
         We then split that line on our DELIMITER - which we are using as ::
         Leaving us with an array of Strings, stored in dvdTokens.
         Which should look like this:
         ______________________________________________________________________
         |               |          | |           |         |                  |
         |American Psycho|05/09/2000|R|Mary Harron|Lionsgate|10/10 A Great Film|
         |               |          | |           |         |                  | 
         -----------------------------------------------------------------------
                [0]           [1]   [2]    [3]        [4]           [5]
        */
        
        String[] dvdTokens = dvdAsText.split(DELIMITER);

        // Given the pattern above, the DVD title is in index 0 of the array.
        String dvdTitle = dvdTokens[0];

        // Which we can then use to create a new DVD object to satisfy
        // the requirements of the Student constructor.
        DVD dvdFromFile = new DVD(dvdTitle);
        
        String dateString = dvdTokens[1];
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM, uuuu");
        LocalDate releaseDate = LocalDate.parse(dateString, dtf);
        dvdFromFile.setReleaseDate(releaseDate);
        
        dvdFromFile.setMPAARating(dvdTokens[2]);
        dvdFromFile.setDirectorsName(dvdTokens[3]);
        dvdFromFile.setStudio(dvdTokens[4]);
        dvdFromFile.setUserReview(dvdTokens[5]);
        
        return dvdFromFile;
    }
    
    private void loadLibrary() throws DVDLibraryDaoException {
        Scanner scanner;
        try {
            File file = new File(LIBRARY_FILE);
            scanner = new Scanner(new FileInputStream(file));
            // Create Scanner for reading the file
            //scanner = new Scanner(new BufferedReader(new FileReader(LIBRARY_FILE)));
        }catch (FileNotFoundException e) {
            throw new DVDLibraryDaoException(
                "-_- Could not load library data into memory.", e);
        }
    
    
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentDVD holds the most recent DVD unmarshalled
        DVD currentDVD;
        // Go through LIBRARY_FILE line by line, decoding each line into a 
        // DVD object by calling the unmarshallDVD method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a DVD object
            currentDVD = unmarshallDVD(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            dvdHashMap.put(currentDVD.getTitle(), currentDVD);
        }
        scanner.close();
    }
    
    
    private String marshallDVD(DVD aDVD){
    // We need to turn a DVD object into a line of text for our file.
    // For example, we need an in memory object to end up like this:
    // The Lion King::15/06/1994::Roger Aller and Bob Minkoff::Buena Vista::9/10 Great movie

    String dvdAsText = aDVD.getTitle()+ DELIMITER;
    dvdAsText += aDVD.getReleaseDate().format(DateTimeFormatter.ofPattern("dd MMMM, uuuu")) + DELIMITER;
    dvdAsText += aDVD.getMPAARating()+ DELIMITER;
    dvdAsText += aDVD.getDirectorsName() + DELIMITER;
    dvdAsText += aDVD.getStudio() + DELIMITER;
    dvdAsText += aDVD.getUserReview();

    return dvdAsText;
    }
    
    /**
     * Writes all DVDs in the roster out to a LIBRARY_FILE. See loadLibrary()
     * for file format.
     *
     * @throws DVDLibraryDaoException if an error occurs writing to the file
     */
    private void writeLibrary() throws DVDLibraryDaoException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(LIBRARY_FILE));
        } catch (IOException e) {
            throw new DVDLibraryDaoException(
                    "Could not save DVD data.", e);
        }

        // Write out the DVD objects to the library file.
        String dvdAsText;
        List<DVD> dvdList = new ArrayList(dvdHashMap.values());
        for (DVD currentDVD : dvdList) {
            // turn a DVD into a String
            dvdAsText = marshallDVD(currentDVD);
            // write the DVD object to the file
            out.println(dvdAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        out.close();
    }

    
    
    @Override
    public List<DVD> getAllMoviesInLastNYears(int nYears) throws DVDLibraryDaoException {
        loadLibrary();
        int startYear = LocalDate.now().getYear() - nYears;
        LocalDate releaseDateStart = LocalDate.of(startYear,01,01);
        
        List<DVD> releaseMoviesInNYears = dvdHashMap.values().stream()
                .filter((dvd) -> dvd.getReleaseDate().isAfter(releaseDateStart))
                .collect(Collectors.toList());
        
        return releaseMoviesInNYears;
    }

    @Override
    public List<DVD> findAllMoviesByMPAARAting(String mPAARating) throws DVDLibraryDaoException{
        loadLibrary();
        List<DVD> dvdListByMPAARating = dvdHashMap.values().stream()
                .filter((dvd) -> dvd.getMPAARating().equals(mPAARating))
                .collect(Collectors.toList());
        
        return dvdListByMPAARating;
    }

    @Override
    public double findAverageAgeOfCollection() throws DVDLibraryDaoException {
        loadLibrary();
        OptionalDouble averageAge = dvdHashMap.values().stream()
                .mapToInt((dvd) -> dvd.getReleaseDate().until(LocalDate.now()).getYears())
                .average();
        
        if (averageAge.isPresent()) {
            return averageAge.getAsDouble();
        }else {
            return 0.0;
        }
        
    }

    @Override
    public DVD findOldestDVD() throws DVDLibraryDaoException {
        loadLibrary();
        DVD oldestDVD = dvdHashMap.values().stream()
                .min(Comparator.comparing(DVD::getReleaseDate)).get();
        
        return oldestDVD;
    }
    
    @Override
    public DVD findNewestDVD() throws DVDLibraryDaoException {
        loadLibrary();
        DVD newestDVD = dvdHashMap.values().stream()
                .max(Comparator.comparing(DVD::getReleaseDate)).get();
        
        return newestDVD;
    }
    
}
