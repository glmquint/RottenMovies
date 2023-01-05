package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewFeedDTO {
    private String movieTitle;
    private String criticName;
    private String content;
    private boolean freshness;
    private Date reviewDate;

    public ReviewFeedDTO(String movieTitle, String criticName, String content, boolean freshness, Date reviewDate) {
        this.movieTitle = movieTitle;
        this.criticName = criticName;
        this.content = content;
        this.freshness = freshness;
        this.reviewDate = reviewDate;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCriticName() {
        return criticName;
    }

    public void setCriticName(String criticName) {
        this.criticName = criticName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFreshness() {
        return freshness;
    }

    public void setFreshness(boolean freshness) {
        this.freshness = freshness;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "ReviewFeedDTO{" +
                "movieTitle='" + movieTitle + '\'' +
                ", criticName='" + criticName + '\'' +
                ", content='" + content + '\'' +
                ", freshness=" + freshness +
                ", reviewDate=" + simpleDateFormat.format(reviewDate) +
                '}';
    }
}
