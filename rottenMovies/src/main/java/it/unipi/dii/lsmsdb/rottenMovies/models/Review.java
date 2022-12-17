package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Review {
    @SerializedName("critic_name")
    private String criticName;
    @SerializedName("movie")
    private String movie;
    @SerializedName("top_critic")
    private boolean topCritic;
    @SerializedName("review_type")
    private String reviewType;
    @SerializedName("review_score")
    private String reviewScore;
    @SerializedName("review_date")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ISO8601Utils reviewDate;
    @SerializedName("review_content")
    private String reviewContent;

    public Review() {
    }

    public String getCriticName() {
        return criticName;
    }

    public void setCriticName(String criticName) {
        this.criticName = criticName;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
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

    public ISO8601Utils getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(ISO8601Utils reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    @Override
    public String toString() {
        Date date = new Date();
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        reviewDate.format(date,false);
        return "Review{" +
                "criticName='" + criticName + '\'' +
                ", movie='" + movie + '\'' +
                ", topCritic=" + topCritic + '\'' +
                ", reviewType='" + reviewType + '\'' +
                ", reviewScore='" + reviewScore + '\'' +
                ", reviewDate=" + df2.format(date) + '\'' +
                ", reviewContent='" + reviewContent + '\'' +
                '}';
    }
}
