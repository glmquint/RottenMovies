package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;
import org.bson.types.ObjectId;
/**
 * <class>SimplyfiedReviewDTO</class> is the container used to pass minified reviews information between
 * the service and presentation layer
 */

public class SimplyfiedReviewDTO {
    private ObjectId movieID;
    private String primaryTitle;
    private int index;

    public SimplyfiedReviewDTO(){}
    public SimplyfiedReviewDTO(SimplyfiedReview s){
        this.movieID=s.getMovieID();
        this.primaryTitle=s.getPrimaryTitle();
        this.index=s.getIndex();
    }
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
