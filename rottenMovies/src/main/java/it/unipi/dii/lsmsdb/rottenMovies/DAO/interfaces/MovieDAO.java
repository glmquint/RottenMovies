package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {
    Movie searchByTitle(String title);
    Movie searchById(ObjectId id);
    List<Movie> searchByYearRange(int startYear, int endYear);
    List<Movie> searchByTopRatings(int rating, boolean type);
    List<Movie> searchByUserRatings(int rating, boolean type);
    Boolean delete(Movie toDelete);
    Boolean update(Movie updated);
    Boolean insert(Movie newOne);
    //List<Movie> searchByYear(int year);
}
