package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

import java.util.List;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO {
    Movie searchByTitle(String title);
    List<Movie> searchByYear(int year);
    Movie searchByTopRatings(int rating);
    Movie searchByUserRatings(int rating);
}
