package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.types.ObjectId;

/**
 * <class>ReviewUserDTO</class> is the container used to pass specific information for review between
 * the service and presentation layer
 */

public class ReviewUserDTO extends ReviewDTO{
    private ObjectId movie_id;
    private String movie;

    public ObjectId getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(ObjectId movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public ReviewUserDTO() {}

    public ReviewUserDTO (Review r){
        super(r);
        this.movie=r.getMovie();
        this.movie_id=r.getMovie_id();
    }
}
