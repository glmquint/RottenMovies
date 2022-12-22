package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.util.LinkedHashMap;

public class SimplyfiedReview {
    @JsonProperty("_id")
    private ObjectId movieID;
    @JsonProperty("primaryTitle")
    private String primaryTitle;
    @JsonProperty("review_index")
    private int index;

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
