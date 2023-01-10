package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewMovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewUserDTO;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
/**
 * <class>Review</class> is the container in which data from the backend for Review is mapped
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    @JsonProperty("_id")
    private ObjectId movie_id;
    @JsonProperty("critic_name")
    private String criticName;
    @JsonProperty("primaryTitle")
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

    public Review (ReviewDTO r){
        this.topCritic=r.isTopCritic();
        this.reviewType=r.getReviewType();
        this.reviewScore=r.getReviewScore();
        this.reviewDate=r.getReviewDate();
        this.reviewContent=r.getReviewContent();
        if(r instanceof ReviewUserDTO){
            this.movie_id=((ReviewUserDTO) r).getMovie_id();
            this.movie= ((ReviewUserDTO) r).getMovie();
        }
        else if (r instanceof ReviewMovieDTO){
            this.criticName=((ReviewMovieDTO)r).getCriticName();
        }
    }

    public ObjectId getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Object id) {
        if(id instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)id;
            this.movie_id = new ObjectId(link.get("$oid").toString());
        }
        else if (id instanceof ObjectId){
            this.movie_id= (ObjectId) id;
        }
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
        if (movie == null) {
            return "Review{" +
                    "criticName='" + criticName + '\'' +
                    ", topCritic=" + topCritic + '\'' +
                    ", reviewType='" + reviewType + '\'' +
                    ", reviewScore='" + reviewScore + '\'' +
                    ", reviewDate=" + df2.format(date) + '\'' +
                    ", reviewContent='" + reviewContent + '\'' +
                    '}';
        } else {
            return "Review{" +
                    "id=" + movie_id.toString() + '\'' +
                    ", movie='" + movie + '\'' +
                    ", topCritic=" + topCritic + '\'' +
                    ", reviewType='" + reviewType + '\'' +
                    ", reviewScore='" + reviewScore + '\'' +
                    ", reviewDate=" + df2.format(date) + '\'' +
                    ", reviewContent='" + reviewContent + '\'' +
                    '}';
        }
    }
}
