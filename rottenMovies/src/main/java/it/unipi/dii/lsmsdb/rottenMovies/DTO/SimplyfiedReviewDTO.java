package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import org.bson.types.ObjectId;

public class SimplyfiedReviewDTO {
    private ObjectId movieID;
    private String primaryTitle;
    private int index;

    public ObjectId getMovieID() {
        return movieID;
    }

    public void setMovieID(ObjectId movieID) {
        this.movieID = movieID;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
