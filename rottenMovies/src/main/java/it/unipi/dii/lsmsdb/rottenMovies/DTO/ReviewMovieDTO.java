package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

public class ReviewMovieDTO extends ReviewDTO{
    private String criticName;

    public ReviewMovieDTO (Review r){
        this.criticName=r.getCriticName();
        this.topCritic=r.isTopCritic();
        this.reviewType=r.getReviewType();
        this.reviewScore=r.getReviewScore();
        this.reviewDate=r.getReviewDate();
        this.reviewContent=r.getReviewContent();
    }
    public String getCriticName() {
        return criticName;
    }

    public void setCriticName(String criticName) {
        this.criticName = criticName;
    }

}
