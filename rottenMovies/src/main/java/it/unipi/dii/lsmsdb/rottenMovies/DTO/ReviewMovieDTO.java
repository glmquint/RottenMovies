package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
/**
 * <class>ReviewDTO</class> is the container used to pass specific review information between
 * the service and presentation layer
 */
public class ReviewMovieDTO extends ReviewDTO{
    private String criticName;

    public ReviewMovieDTO() {}

    public ReviewMovieDTO (Review r){
        super(r);
        this.criticName=r.getCriticName();
    }
    public String getCriticName() {
        return criticName;
    }

    public void setCriticName(String criticName) {
        this.criticName = criticName;
    }

}
