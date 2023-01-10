package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.SimplyfiedReviewDTO;
import org.bson.types.ObjectId;

import java.util.LinkedHashMap;
/**
 * <class>SimplyfiedReview</class> is the container in which data from the backend for SimplyfiedReview is mapped
 */
public class SimplyfiedReview {
    @JsonProperty("movie_id")
    private ObjectId movieID;
    @JsonProperty("primaryTitle")
    private String primaryTitle;
    @JsonProperty("review_index")
    private int index;

    public SimplyfiedReview() {}
    public SimplyfiedReview (SimplyfiedReviewDTO s){
        this.movieID=s.getMovieID();
        this.primaryTitle=s.getPrimaryTitle();
        this.index=s.getIndex();
    }
    public ObjectId getMovieID() {
        return this.movieID;
    }

    public void setMovieID(Object movieID) {
        if(movieID instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)movieID;
            this.movieID = new ObjectId(link.get("$oid").toString());
        }
    }

    public String getPrimaryTitle() {
        return this.primaryTitle;
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

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public String toString() {
        return "SimplyfiedReview{" +
                "movieID='" + getMovieID() + '\'' +
                ", primaryTitle='" + getPrimaryTitle() + '\'' +
                ", index=" + getIndex() +
                '}';
    }
}
