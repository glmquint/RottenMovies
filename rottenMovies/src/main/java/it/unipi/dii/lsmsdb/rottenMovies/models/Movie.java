package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @SerializedName("primaryTitle")
    private String primaryTitle;
    @SerializedName("year")
    private int year;
    @SerializedName("runtimeMinutes")
    private int runtimeMinutes;
    @SerializedName("genres")
    private ArrayList<String> genres;
    @SerializedName("production_company")
    private String productionCompany;
    //@JsonProperty("tomatometer_status")
    @SerializedName("tomatometer_status")
    private String tomatometerStatus;
    @SerializedName("tomatometer_rating")
    private int tomatometerRating;
    @SerializedName("audience_status")
    private String audienceStatus;
    @SerializedName("audience_rating")
    private int audienceRating;
    @SerializedName("audience_count")
    private int audienceCount;
    @SerializedName("tomatometer_fresh_critics_count")
    private int tomatometerFreshCriticsCount;
    @SerializedName("tomatometer_rotten_critics_count")
    private int tomatometerRottenCriticsCount;
    @SerializedName("personnel")
    private ArrayList<Personnel> personnel;
    @SerializedName("review")
    private ArrayList<Review> reviews;

    @SerializedName("critic_consensus")
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

    public int gettomatometerRating() {
        return tomatometerRating;
    }

    public void settomatometerRating(int tomatometerRating) {
        this.tomatometerRating = tomatometerRating;
    }

    public String getAudienceStatus() {
        return audienceStatus;
    }

    public void setAudienceStatus(String audienceStatus) {
        this.audienceStatus = audienceStatus;
    }

    public int getaudienceRating() {
        return audienceRating;
    }

    public void setaudienceRating(int audienceRating) {
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

    public String getCriticConsensus() {
        return criticConsensus;
    }

    public void setCriticConsensus(String criticConsensus) {
        this.criticConsensus = criticConsensus;
    }

    public ArrayList<Personnel> getpersonnel() {
        return personnel;
    }

    public void setpersonnel(ArrayList<Personnel> personnel) {
        this.personnel = personnel;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "primaryTitle='" + primaryTitle + '\'' +
                ", year=" + year +
                ", runtimeMinutes=" + runtimeMinutes +
                ", genres=" + genres +
                ", productionCompany='" + productionCompany + '\'' +
                ", tomatometerStatus='" + tomatometerStatus + '\'' +
                ", tomatometerRating=" + tomatometerRating +
                ", audienceStatus=" + audienceStatus +
                ", audienceRating=" + audienceRating +
                ", audienceCount=" + audienceCount +
                ", tomatometerFreshCriticsCount=" + tomatometerFreshCriticsCount +
                ", tomatometerRottenCriticsCount=" + tomatometerRottenCriticsCount +
                ", personnel=" + personnel +
                ", reviews=" + reviews +
                ", criticConsensus='" + criticConsensus + '\'' +
                '}';
    }
}
