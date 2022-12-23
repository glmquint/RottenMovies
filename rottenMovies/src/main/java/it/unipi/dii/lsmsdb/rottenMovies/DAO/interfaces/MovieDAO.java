package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {
    MovieDTO searchByTitle(String title) throws DAOException;
    MovieDTO searchById(ObjectId id) throws DAOException;
    List<MovieDTO> searchByYearRange(int startYear, int endYear) throws DAOException;
    List<MovieDTO> searchByTopRatings(int rating, boolean type) throws DAOException;
    List<MovieDTO> searchByUserRatings(int rating, boolean type) throws DAOException;
    Boolean delete(MovieDTO toDelete) throws DAOException;
    Boolean update(MovieDTO updated) throws DAOException;
    Boolean insert(MovieDTO newOne) throws DAOException;
    //List<Movie> searchByYear(int year) throws DAOException;

}
