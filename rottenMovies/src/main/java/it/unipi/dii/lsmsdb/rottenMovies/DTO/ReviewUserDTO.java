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

    public ReviewUserDTO() {
    }

    public ReviewUserDTO (Review r){
        this.movie=r.getMovie();
        this.topCritic=r.isTopCritic();
        this.reviewType=r.getReviewType();
        this.reviewScore=r.getReviewScore();
        this.reviewDate=r.getReviewDate();
        this.reviewContent=r.getReviewContent();
    }
}
