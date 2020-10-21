/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary.dao;

import com.mthree.dvdlibrary.dto.DVD;
import java.util.List;

/**
 *
 * @author Haroon
 */
public interface DVDLibraryDao {
    
    /**
     * Adds the given DVD to the library and associates it with the given title.
     * If there is already a DVD associated with the given title, it will return 
     * that DVD object, otherwise it will return null
     * 
     * @param title with which the DVD is to be associated with
     * @param dvd DVD to be added to the library
     * @return the DVD object previously associated with given DVD title 
     * if it exists, null otherwise
     */
    DVD addDVD(String title, DVD dvd) throws DVDLibraryDaoException;
    
    /**
     * Returns a list of all DVDs in the library
     * 
     * @return List containing all DVDs in the library
     */
    List<DVD> getAllDVDS() throws DVDLibraryDaoException;
    
    /**
     * Returns the DVD object associated with the given title.
     * Returns null if no such DVD exists
     * 
     * @param title of the DVD to retrieve
     * @return the DVD object associated with the given title, null if no such DVD exists
     */
    DVD getDVD(String title) throws DVDLibraryDaoException;
    
    /**
     * Removes from the library the DVD associated with the given title.
     * Returns the DVD object that is being removed or null if there is no 
     * DVD object associated with the given title
     * 
     * @param title of the DVD to be removed
     * @return DVD object that was removed or null if no DVD was associated 
     * with the given title
     */
    DVD removeDVD(String title) throws DVDLibraryDaoException;
    
    
    /**
     * Edits the information in the library  for the DVD associated with the given title.
     * Returns the DVD object that is being edited or null if there is no 
     * DVD object associated with the given title
     * 
     * @param title of the DVD to be edited
     * @return DVD object that was edited or null if no DVD was associated 
     * with the given title
     */
    DVD editDVD(String title, DVD editedDVD) throws DVDLibraryDaoException;
    
    
    /**
     *  Returns a list of DVDs that were released in the last N years
     * @param nYears the number of years for which to look for DVDs since 
     * @return a list of DVDs that were released in the last N years
     */
    List<DVD> getAllMoviesInLastNYears(int nYears)throws DVDLibraryDaoException;
    
    /**
     * Returns a list of DVDs with a given rating
     * @param mPAARating the rating that all the DVDs must have
     * @return a list of DVDs which satisfy the condition
     */
    List<DVD> findAllMoviesByMPAARAting(String mPAARating)throws DVDLibraryDaoException;
    
    /**
     * Returns the average age of all the movies in the collection;
     * @return 
     */
    double findAverageAgeOfCollection() throws DVDLibraryDaoException;
    
    /**
     * 
     * @return
     * @throws DVDLibraryDaoException 
     */
    DVD findOldestDVD() throws DVDLibraryDaoException;
    
    /**
     * 
     * @return
     * @throws DVDLibraryDaoException 
     */
    DVD findNewestDVD() throws DVDLibraryDaoException;
    
}
