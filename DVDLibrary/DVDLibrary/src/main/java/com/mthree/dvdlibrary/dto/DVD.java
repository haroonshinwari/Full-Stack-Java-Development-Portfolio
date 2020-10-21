/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.dvdlibrary.dto;

import java.time.LocalDate;

/**
 *
 * @author Haroon
 */
public class DVD {
    
    String title;
    LocalDate releaseDate;
    String MPAARating;
    String directorsName;
    String studio;
    String userReview;

    public DVD(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMPAARating() {
        return MPAARating;
    }

    public void setMPAARating(String mPAARating) {
        this.MPAARating = mPAARating;
    }

    public String getDirectorsName() {
        return directorsName;
    }

    public void setDirectorsName(String directorsName) {
        this.directorsName = directorsName;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

}
