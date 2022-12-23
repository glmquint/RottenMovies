package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class MovieDTO {
        private ObjectId id;
        private String primaryTitle;

        private int year;

        private int runtimeMinutes;

        private ArrayList<String> genres;

        private String productionCompany;

        private String tomatometerStatus;

        private int tomatometerRating;

        private String audienceStatus;

        private int audienceRating;

        private int audienceCount;

        private int tomatometerFreshCriticsCount;

        private int tomatometerRottenCriticsCount;

        private ArrayList<PersonnelDTO> personnel;

        private ArrayList<ReviewMovieDTO> reviews;

        private String criticConsensus;

    public MovieDTO() {
    }

    public MovieDTO(Movie movie){
        this.id=movie.getId();
        this.primaryTitle=movie.getPrimaryTitle();
        this.year= movie.getYear();
        this.runtimeMinutes=movie.getRuntimeMinutes();
        this.genres=movie.getGenres();
        this.productionCompany=movie.getProductionCompany();
        this.tomatometerStatus=movie.getTomatometerStatus();
        this.tomatometerRating=movie.gettomatometerRating();
        this.audienceStatus=movie.getAudienceStatus();
        this.audienceRating=movie.getaudienceRating();
        this.audienceCount=movie.getAudienceCount();
        this.tomatometerFreshCriticsCount=movie.getTomatometerFreshCriticsCount();
        this.tomatometerRottenCriticsCount= movie.getTomatometerRottenCriticsCount();
        this.criticConsensus=movie.getCriticConsensus();
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

    public String getTomatometerStatus() {
        return tomatometerStatus;
    }

    public void setTomatometerStatus(String tomatometerStatus) {
        this.tomatometerStatus = tomatometerStatus;
    }

    public int getTomatometerRating() {
        return tomatometerRating;
    }

    public void setTomatometerRating(int tomatometerRating) {
        this.tomatometerRating = tomatometerRating;
    }

    public String getAudienceStatus() {
        return audienceStatus;
    }

    public void setAudienceStatus(String audienceStatus) {
        this.audienceStatus = audienceStatus;
    }

    public int getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(int audienceRating) {
        this.audienceRating = audienceRating;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }

    public int getTomatometerFreshCriticsCount() {
        return tomatometerFreshCriticsCount;
    }

    public void setTomatometerFreshCriticsCount(int tomatometerFreshCriticsCount) {
        this.tomatometerFreshCriticsCount = tomatometerFreshCriticsCount;
    }

    public int getTomatometerRottenCriticsCount() {
        return tomatometerRottenCriticsCount;
    }

    public void setTomatometerRottenCriticsCount(int tomatometerRottenCriticsCount) {
        this.tomatometerRottenCriticsCount = tomatometerRottenCriticsCount;
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

    public String getCriticConsensus() {
        return criticConsensus;
    }

    public void setCriticConsensus(String criticConsensus) {
        this.criticConsensus = criticConsensus;
    }

}
