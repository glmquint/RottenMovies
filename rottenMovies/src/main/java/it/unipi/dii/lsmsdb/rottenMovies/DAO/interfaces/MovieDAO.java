package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

import java.util.List;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO {
    Movie searchByTitle(String title);
    List<Movie> searchByYearRange(int startYear, int endYear);
    List<Movie> searchByTopRatings(int rating, boolean type);
    List<Movie> searchByUserRatings(int rating, boolean type);
    void deleteMovie(String title);
    Boolean updateMovie(Movie updated);
    Boolean insertMovie(Movie newOne);
    //List<Movie> searchByYear(int year);
}
