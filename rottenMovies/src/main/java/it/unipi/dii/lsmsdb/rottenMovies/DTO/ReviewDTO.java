package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

import java.util.Date;
/**
 * <class>ReviewDTO</class> is the container used to pass review information between
 * the service and presentation layer
 */
public abstract class ReviewDTO {
    protected boolean topCritic;
    protected String reviewType;
    protected String reviewScore;
    protected Date reviewDate;
    protected String reviewContent;

    public ReviewDTO() {}
    public ReviewDTO(Review r) {
        this.topCritic=r.isTopCritic();
        this.reviewType=r.getReviewType();
        this.reviewScore=r.getReviewScore();
        this.reviewDate=r.getReviewDate();
        this.reviewContent=r.getReviewContent();
    }

    public boolean isTopCritic() {
        return topCritic;
    }

    public void setTopCritic(boolean topCritic) {
        this.topCritic = topCritic;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(String reviewScore) {
        this.reviewScore = reviewScore;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
