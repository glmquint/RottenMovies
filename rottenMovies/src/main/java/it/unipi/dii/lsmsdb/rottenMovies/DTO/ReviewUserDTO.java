package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

public class ReviewUserDTO extends ReviewDTO{
    private String movie;

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
    }
}
