package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <class>ReviewFeedDTO</class> is the container used to pass information for the feed functionality between
 * the service and presentation layer
 */
public class ReviewFeedDTO {
    public String criticId;
    private String movieTitle;
    private String movieId;
    private String criticName;
    private String content;
    private boolean freshness;
    private Date reviewDate;

    public ReviewFeedDTO(String criticId, String movieTitle, String movieId, String criticName, String content, boolean freshness, Date reviewDate) {
        this.criticId = criticId;
        this.movieTitle = movieTitle;
        this.movieId = movieId;
        this.criticName = criticName;
        this.content = content;
        this.freshness = freshness;
        this.reviewDate = reviewDate;
    }

    public String getId() {
        return criticId;
    }

    public void setId(String criticId) {
        this.criticId = criticId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieId() {return movieId;}

    public void setMovieId(String movieId) {this.movieId = movieId;}

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
                "id= " + criticId + '\'' +
                "movieTitle='" + movieTitle + '\'' +
                ", criticName='" + criticName + '\'' +
                ", content='" + content + '\'' +
                ", freshness=" + freshness +
                ", reviewDate=" + simpleDateFormat.format(reviewDate) +
                '}';
    }
}
