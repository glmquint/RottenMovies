package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PersonnelDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewMovieDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * <class>Movie</class> is the container in which data from the backend for Movie is mapped
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @JsonProperty("_id")
    private ObjectId id;
    @JsonProperty("primaryTitle")
    private String primaryTitle;
    @JsonProperty("year")
    private int year;
    @JsonProperty("runtimeMinutes")
    private int runtimeMinutes;
    @JsonProperty("genres")
    private ArrayList<String> genres;
    @JsonProperty("production_company")
    private String productionCompany;
    @JsonProperty("top_critic_status")
    private String top_critic_status;
    @JsonProperty("top_critic_rating")
    private int top_critic_rating;
    @JsonProperty("user_status")
    private String user_status;
    @JsonProperty("user_rating")
    private int user_rating;
    @JsonProperty("user_fresh_count")
    private int user_fresh_count;
    @JsonProperty("user_rotten_count")
    private int user_rotten_count;
    @JsonProperty("top_critic_fresh_count")
    private int top_critic_fresh_count;
    @JsonProperty("top_critic_rotten_count")
    private int top_critic_rotten_count;
    @JsonProperty("personnel")
    private ArrayList<Personnel> personnel;
    @JsonProperty("review")
    private ArrayList<Review> reviews;
    //@JsonProperty("critic_consensus")
    //private String criticConsensus;
    @JsonProperty("poster_url")
    private String posterUrl;


    public Movie() {
    }
    public Movie (MovieDTO moviedto) {
        this.id = moviedto.getId();
        this.primaryTitle = moviedto.getPrimaryTitle();
        this.year = moviedto.getYear();
        this.runtimeMinutes = moviedto.getRuntimeMinutes();
        this.genres = moviedto.getGenres();
        this.productionCompany = moviedto.getProductionCompany();
        this.top_critic_status = moviedto.getTop_critic_status();
        this.top_critic_rating = moviedto.getTop_critic_rating();
        this.user_status = moviedto.getUser_status();
        this.user_rating = moviedto.getUser_rating();
        this.user_fresh_count = moviedto.getUser_fresh_count();
        this.top_critic_fresh_count = moviedto.getTop_critic_fresh_count();
        this.top_critic_rotten_count = moviedto.getTop_critic_rotten_count();
        this.posterUrl= moviedto.getPosterUrl();
        //this.criticConsensus = moviedto.getCriticConsensus();
        ArrayList<ReviewMovieDTO> reviewsDTO = moviedto.getReviews();
        ArrayList<Review> reviews = null;
        if (reviewsDTO != null){
            reviews = new ArrayList<Review>();
            Review review = null;
            for (ReviewMovieDTO r : reviewsDTO) {
                review = new Review(r);
                reviews.add(review);
            }
        }
        this.reviews=reviews;
        ArrayList<PersonnelDTO> personnelDTO=moviedto.getPersonnel();
        ArrayList<Personnel> personnel = null;
        if (personnelDTO != null){
            personnel = new ArrayList<Personnel>();
            Personnel p;
            for(PersonnelDTO per: personnelDTO){
                p=new Personnel(per);
                personnel.add(p);
            }
        }
        this.personnel=personnel;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(Object id) {
        if(id instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)id;
            this.id = new ObjectId(link.get("$oid").toString());
        }
        else if (id instanceof ObjectId){
            this.id= (ObjectId) id;
        }
    }

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
            this.runtimeMinutes = -1;
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

    public String getTop_critic_status() {
        return top_critic_status;
    }

    public void setTop_critic_status(String top_critic_status) {
        this.top_critic_status = top_critic_status;
    }

    public int getTop_critic_rating() {
        return top_critic_rating;
    }

    public void setTop_critic_rating(Object top_critic_rating) {
        if(top_critic_rating instanceof  Integer)
            this.top_critic_rating = (int)top_critic_rating;
        else
            this.top_critic_rating = 0;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public int getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(Object user_rating) {
        if(user_rating instanceof  Integer)
            this.user_rating = (int)user_rating;
        else
            this.user_rating = 0;
    }

    public int getUser_fresh_count() {
        return user_fresh_count;
    }

    public void setUser_fresh_count(Object user_fresh_count) {
        if(user_fresh_count instanceof  Integer)
            this.user_fresh_count = (int) user_fresh_count;
        else
            this.user_fresh_count = 0;
    }

    public int getUser_rotten_count() {
        return user_rotten_count;
    }

    public void setUser_rotten_count(Object user_rotten_count) {
        if(user_rotten_count instanceof  Integer)
            this.user_rotten_count = (int) user_rotten_count;
        else
            this.user_rotten_count = 0;
    }

    public int getTop_critic_fresh_count() {
        return top_critic_fresh_count;
    }

    public void setTop_critic_fresh_count(int top_critic_fresh_count) {
        this.top_critic_fresh_count = top_critic_fresh_count;
    }

    public int getTop_critic_rotten_count() {
        return top_critic_rotten_count;
    }

    public void setTop_critic_rotten_count(int top_critic_rotten_count) {
        this.top_critic_rotten_count = top_critic_rotten_count;
    }

    /*
    public String getCriticConsensus() {
        return criticConsensus;
    }

    public void setCriticConsensus(String criticConsensus) {
        this.criticConsensus = criticConsensus;
    }

     */

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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        if (id==null){
            return "Movie{}";
        }
        return "Movie{" +
                "id=" + id.toString() +
                ", primaryTitle='" + primaryTitle + '\'' +
                ", year=" + year +
                ", runtimeMinutes=" + runtimeMinutes +
                ", genres=" + genres +
                ", productionCompany='" + productionCompany + '\'' +
                ", top_critic_status='" + top_critic_status + '\'' +
                ", top_critic_rating=" + top_critic_rating +
                ", user_status='" + user_status + '\'' +
                ", user_rating=" + user_rating +
                ", user_fresh_count=" + user_fresh_count +
                ", user_rotten_count=" + user_rotten_count +
                ", top_critic_fresh_count=" + top_critic_fresh_count +
                ", top_critic_rotten_count=" + top_critic_rotten_count +
                ", personnel=" + personnel +
                ", reviews=" + reviews +
                ", posterUrl=" + posterUrl +
                '}';
    }
}
