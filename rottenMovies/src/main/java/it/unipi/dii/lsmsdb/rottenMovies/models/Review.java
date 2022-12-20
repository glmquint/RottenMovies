package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    @JsonProperty("critic_name")
    private String criticName;
    @JsonProperty("movie")
    private String movie;
    @JsonProperty("top_critic")
    private boolean topCritic;
    @JsonProperty("review_type")
    private String reviewType;
    @JsonProperty("review_score")
    private String reviewScore;
    @JsonProperty("review_date")
    private Date reviewDate;
    @JsonProperty("review_content")
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

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Object reviewDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(reviewDate instanceof LinkedHashMap<?,?>)
            try {
                LinkedHashMap link = (LinkedHashMap)reviewDate;
                //System.out.println(link.get("$date"));
                //System.out.println(link.get("$numberLong"));
                //System.out.println(link.get("$date").getClass());
                if(link.get("$date")!=null) {
                    if(link.get("$date") instanceof LinkedHashMap<?,?>) {
                        this.reviewDate = new Date(1970, 1, 1);
                    }
                    else{
                        this.reviewDate = formatter.parse(link.get("$date").toString());
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
        }
        else if (reviewDate instanceof String) {
            try {
                this.reviewDate = formatter.parse((String) reviewDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else if (reviewDate instanceof Date){
            setReviewDate_date((Date)reviewDate);
        }

    }


    public void setReviewDate_date(Date reviewDate) {
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
