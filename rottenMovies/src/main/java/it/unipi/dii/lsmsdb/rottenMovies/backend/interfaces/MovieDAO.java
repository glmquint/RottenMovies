package it.unipi.dii.lsmsdb.rottenMovies.backend.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO {
    Movie searchByTitle(String title);
    Movie searchByYear(int year);
    Movie searchByTopRatings(int rating);
    Movie searchByUserRatings(int rating);
}
