package it.unipi.dii.lsmsdb.rottenMovies.models;

import java.util.ArrayList;

public class Movie {
    private String primaryTitle;
    private int year;
    private int runtimeMinutes;
    private ArrayList<String> genres;
    private String productionCompany;
    private String tomatometerStatus;
    private int tomatometerRatings;
    private int audienceStatus;
    private int audienceRatings;
    private int audienceCount;
    private int tomatometerFreshCriticsCount;
    private int tomatometerRottenCriticsCount;

    private ArrayList<Personnel> personnels;
    private ArrayList<Review> reviews;

    private String criticConsensus;

    public Movie() {}

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public String getTomatometerStatus() {
        return tomatometerStatus;
    }

    public void setTomatometerStatus(String tomatometerStatus) {
        this.tomatometerStatus = tomatometerStatus;
    }

    public int getTomatometerRatings() {
        return tomatometerRatings;
    }

    public void setTomatometerRatings(int tomatometerRatings) {
        this.tomatometerRatings = tomatometerRatings;
    }

    public int getAudienceStatus() {
        return audienceStatus;
    }

    public void setAudienceStatus(int audienceStatus) {
        this.audienceStatus = audienceStatus;
    }

    public int getAudienceRatings() {
        return audienceRatings;
    }

    public void setAudienceRatings(int audienceRatings) {
        this.audienceRatings = audienceRatings;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }

    public int getTomatometerFreshCriticsCount() {
        return tomatometerFreshCriticsCount;
    }

    public void setTomatometerFreshCriticsCount(int tomatometerFreshCriticsCount) {
        this.tomatometerFreshCriticsCount = tomatometerFreshCriticsCount;
    }

    public int getTomatometerRottenCriticsCount() {
        return tomatometerRottenCriticsCount;
    }

    public void setTomatometerRottenCriticsCount(int tomatometerRottenCriticsCount) {
        this.tomatometerRottenCriticsCount = tomatometerRottenCriticsCount;
    }

    public String getCriticConsensus() {
        return criticConsensus;
    }

    public void setCriticConsensus(String criticConsensus) {
        this.criticConsensus = criticConsensus;
    }

    public ArrayList<Personnel> getPersonnels() {
        return personnels;
    }

    public void setPersonnels(ArrayList<Personnel> personnels) {
        this.personnels = personnels;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
