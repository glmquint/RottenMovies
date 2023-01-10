package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.types.ObjectId;

import java.util.ArrayList;
/**
 * <class>MovieDTO</class> is the container used to pass data regarding movie information between
 * the service and presentation layer
 */
public class MovieDTO {
        private ObjectId id;
        private String primaryTitle;

        private int year;

        private int runtimeMinutes;

        private ArrayList<String> genres;

        private String productionCompany;

        private String top_critic_status;

        private int top_critic_rating;

        private String user_status;

        private int user_rating;

        private int user_fresh_count;

        private int user_rotten_count;

        private int top_critic_fresh_count;

        private int top_critic_rotten_count;

        private ArrayList<PersonnelDTO> personnel;

        private ArrayList<ReviewMovieDTO> reviews;


        private String posterUrl;
    public MovieDTO() {
    }

    public MovieDTO(Movie movie){
        this.id=movie.getId();
        this.primaryTitle=movie.getPrimaryTitle();
        this.year= movie.getYear();
        this.runtimeMinutes=movie.getRuntimeMinutes();
        this.genres=movie.getGenres();
        this.productionCompany=movie.getProductionCompany();
        this.top_critic_status=movie.getTop_critic_status();
        this.top_critic_rating=movie.getTop_critic_rating();
        this.user_status=movie.getUser_status();
        this.user_rating=movie.getUser_rating();
        this.user_fresh_count=movie.getUser_fresh_count();
        this.user_rotten_count=movie.getUser_rotten_count();
        this.top_critic_fresh_count=movie.getTop_critic_fresh_count();
        this.top_critic_rotten_count= movie.getTop_critic_rotten_count();
        this.posterUrl=movie.getPosterUrl();
        ArrayList<Review> reviews=movie.getReviews();
        ArrayList<ReviewMovieDTO> reviewsdto = null;
        if (reviews != null) {
            reviewsdto = new ArrayList<ReviewMovieDTO>();
            ReviewMovieDTO reviewdto;
            for (Review r : reviews) {
                reviewdto = new ReviewMovieDTO(r);
                reviewsdto.add(reviewdto);
            }
        }
        this.reviews=reviewsdto;
        ArrayList<Personnel> personnel=movie.getpersonnel();
        ArrayList<PersonnelDTO> personneldto = null;
        if (personnel != null) {
            personneldto = new ArrayList<PersonnelDTO>();
            PersonnelDTO p;
            for (Personnel per : personnel) {
                p = new PersonnelDTO(per);
                personneldto.add(p);
            }
        }
        this.personnel=personneldto;
    }
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public String getTop_critic_status() {
        return top_critic_status;
    }

    public void setTop_critic_status(String top_critic_status) {
        this.top_critic_status = top_critic_status;
    }

    public int getTop_critic_rating() {
        return top_critic_rating;
    }

    public void setTop_critic_rating(int top_critic_rating) {
        this.top_critic_rating = top_critic_rating;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public int getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(int user_rating) {
        this.user_rating = user_rating;
    }

    public int getUser_fresh_count() {
        return user_fresh_count;
    }

    public void setUser_fresh_count(int user_fresh_count) {
        this.user_fresh_count = user_fresh_count;
    }

    public int getUser_rotten_count() {
        return user_rotten_count;
    }

    public void setUser_rotten_count(int user_rotten_count) {
        this.user_rotten_count = user_rotten_count;
    }

    public int getTop_critic_fresh_count() {
        return top_critic_fresh_count;
    }

    public void setTop_critic_fresh_count(int top_critic_fresh_count) {
        this.top_critic_fresh_count = top_critic_fresh_count;
    }

    public int getTop_critic_rotten_count() {
        return top_critic_rotten_count;
    }

    public void setTop_critic_rotten_count(int top_critic_rotten_count) {
        this.top_critic_rotten_count = top_critic_rotten_count;
    }

    public ArrayList<PersonnelDTO> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(ArrayList<PersonnelDTO> personnel) {
        this.personnel = personnel;
    }

    public ArrayList<ReviewMovieDTO> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewMovieDTO> reviews) {
        this.reviews = reviews;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
