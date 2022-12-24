package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {
    MongoCollection<Document> getCollection() throws DAOException;
    ArrayList<MovieDTO> executeSearchQuery(int page) throws DAOException;
    Boolean executeDeleteQuery() throws DAOException;
    void queryBuildSearchByTitle (String title) throws DAOException;
    void queryBuildSearchByTitleContains(String title) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByYear(int year, boolean afterYear) throws DAOException;
    void queryBuildSearchByTopRatings(int rating, boolean type) throws DAOException;
    void queryBuildsearchByUserRatings(int rating, boolean type) throws DAOException;
    Boolean update(MovieDTO updated) throws DAOException;
    Boolean insert(MovieDTO newOne) throws DAOException;
    //List<Movie> searchByYear(int year) throws DAOException;

}
