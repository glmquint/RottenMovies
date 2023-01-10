package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.time.LocalDate;
/**
 * <class>MovieReviewBombingDTO</class> is the container used to pass data regarding the review bombing checking functionalities between
 * the service and presentation layer
 */
public class MovieReviewBombingDTO {
    String movieTitle;
    int storicCount;
    int storicRate;
    int targetCount;
    int targetRate;
    LocalDate date;

    public MovieReviewBombingDTO() {
    }

    public MovieReviewBombingDTO(String movieTitle, int storicCount, int storicRate, int targetCount, int targetRate, LocalDate date) {
        this.movieTitle = movieTitle;
        this.storicCount = storicCount;
        this.storicRate = storicRate;
        this.targetCount = targetCount;
        this.targetRate = targetRate;
        this.date = date;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getStoricCount() {
        return storicCount;
    }

    public void setStoricCount(int storicCount) {
        this.storicCount = storicCount;
    }

    public double getStoricRate() {
        return storicRate;
    }

    public void setStoricRate(int storicRate) {
        this.storicRate = storicRate;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public double getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(int targetRate) {
        this.targetRate = targetRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MovieReviewBombingDTO{" +
                "movieTitle='" + movieTitle + '\'' +
                ", storicCount=" + storicCount +
                ", storicRate=" + storicRate +
                ", targetCount=" + targetCount +
                ", targetRate=" + targetRate +
                ", date=" + date +
                '}';
    }
}
