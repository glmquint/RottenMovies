package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.util.ArrayList;

public class MovieDTO {
        private String primaryTitle;

        private int year;

        private int runtimeMinutes;

        private ArrayList<String> genres;

        private String productionCompany;

        private String tomatometerStatus;

        private int tomatometerRating;

        private String audienceStatus;

        private int audienceRating;

        private int audienceCount;

        private int tomatometerFreshCriticsCount;

        private int tomatometerRottenCriticsCount;

        private ArrayList<PersonnelDTO> personnel;

        private ArrayList<ReviewMovieDTO> reviews;

        private String criticConsensus;

    public MovieDTO() {
    }

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

    public int getTomatometerRating() {
        return tomatometerRating;
    }

    public void setTomatometerRating(int tomatometerRating) {
        this.tomatometerRating = tomatometerRating;
    }

    public String getAudienceStatus() {
        return audienceStatus;
    }

    public void setAudienceStatus(String audienceStatus) {
        this.audienceStatus = audienceStatus;
    }

    public int getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(int audienceRating) {
        this.audienceRating = audienceRating;
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

    public ArrayList<PersonnelDTO> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(ArrayList<PersonnelDTO> personnel) {
        this.personnel = personnel;
    }

    public ArrayList<ReviewMovieDTO> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewMovieDTO> reviews) {
        this.reviews = reviews;
    }

    public String getCriticConsensus() {
        return criticConsensus;
    }

    public void setCriticConsensus(String criticConsensus) {
        this.criticConsensus = criticConsensus;
    }
}
