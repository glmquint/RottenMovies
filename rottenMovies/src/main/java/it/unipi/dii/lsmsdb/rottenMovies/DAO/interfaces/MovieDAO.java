package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {
    Movie searchByTitle(String title) throws DAOException;
    Movie searchById(ObjectId id) throws DAOException;
    List<Movie> searchByYearRange(int startYear, int endYear) throws DAOException;
    List<Movie> searchByTopRatings(int rating, boolean type) throws DAOException;
    List<Movie> searchByUserRatings(int rating, boolean type) throws DAOException;
    Boolean delete(Movie toDelete) throws DAOException;
    Boolean update(Movie updated) throws DAOException;
    Boolean insert(Movie newOne) throws DAOException;
    //List<Movie> searchByYear(int year) throws DAOException;
}
