package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @JsonProperty("primaryTitle")
    private String primaryTitle;
    //@JsonProperty("year")
    @JsonProperty("year")
    private int year;
    @JsonProperty("runtimeMinutes")
    private int runtimeMinutes;
    @JsonProperty("genres")
    private ArrayList<String> genres;
    @JsonProperty("production_company")
    private String productionCompany;
    //@JsonProperty("tomatometer_status")
    @JsonProperty("tomatometer_status")
    private String tomatometerStatus;
    @JsonProperty("tomatometer_rating")
    private int tomatometerRating;
    @JsonProperty("audience_status")
    private String audienceStatus;
    @JsonProperty("audience_rating")
    private int audienceRating;
    @JsonProperty("audience_count")
    private int audienceCount;
    @JsonProperty("tomatometer_fresh_critics_count")
    private int tomatometerFreshCriticsCount;
    @JsonProperty("tomatometer_rotten_critics_count")
    private int tomatometerRottenCriticsCount;
    @JsonProperty("personnel")
    private ArrayList<Personnel> personnel;
    @JsonProperty("review")
    private ArrayList<Review> reviews;

    @JsonProperty("critic_consensus")
    private String criticConsensus;

    public Movie() {}

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(Object primaryTitle) {
        if(primaryTitle instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)primaryTitle;
            this.primaryTitle = link.get("$numberDouble").toString();
        }
        else if(primaryTitle instanceof Integer) {
            this.primaryTitle = Integer.toString(((int)primaryTitle));
        }
        else{
            this.primaryTitle = (String)primaryTitle;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(Object year) {
        if(year instanceof Integer)
            this.year = (int)year;
        else
            this.year = -1;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Object runtimeMinutes) {
        if(runtimeMinutes instanceof  Integer)
            this.runtimeMinutes = (int)runtimeMinutes;
        else
            this.runtimeMinutes = 0;
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

    public void settomatometerRating(Object tomatometerRating) {
        if(tomatometerRating instanceof  Integer)
            this.tomatometerRating = (int)tomatometerRating;
        else
            this.tomatometerRating = 0;
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

    public void setaudienceRating(Object audienceRating) {
        if(audienceRating instanceof  Integer)
            this.audienceRating = (int)audienceRating;
        else
            this.audienceRating = 0;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(Object audienceCount) {
        if(audienceCount instanceof  Integer)
            this.audienceCount = (int)audienceCount;
        else
            this.audienceCount = 0;
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

    public String lessDataString(){
        return "Movie{"+'\n'+
                "primaryTitle='" + primaryTitle + '\n' +
                "year=" + year + '\n' +
                "runtimeMinutes=" + runtimeMinutes + '\n' +
                "genres=" + genres + '\n' +
                "productionCompany='" + productionCompany + '\n' +
                "tomatometerStatus='" + tomatometerStatus + '\n' +
                "tomatometerRating=" + tomatometerRating + '\n' +
                "audienceStatus=" + audienceStatus + '\n' +
                "audienceRating=" + audienceRating + '\n' +
                "audienceCount=" + audienceCount + '\n' +
                "tomatometerFreshCriticsCount=" + tomatometerFreshCriticsCount + '\n' +
                "tomatometerRottenCriticsCount=" + tomatometerRottenCriticsCount + '\n' +
                "}";
    }
}
