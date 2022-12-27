package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.ArrayList;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {
    MongoCollection<Document> getCollection() throws DAOException;
    ArrayList<MovieDTO> executeSearchQuery(int page) throws DAOException;
    boolean executeDeleteQuery() throws DAOException;
    void queryBuildSearchByTitle (String title) throws DAOException;
    void queryBuildSearchByTitleContains(String title) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByYear(int year, boolean afterYear) throws DAOException;
    void queryBuildSearchByTopRatings(int rating, boolean type) throws DAOException;
    void queryBuildsearchByUserRatings(int rating, boolean type) throws DAOException;
    boolean update(Movie updated) throws DAOException;
    boolean insert(Movie newOne) throws DAOException;
    boolean delete (Movie movie) throws DAOException;

    //public Boolean insertNeo4j(String id, String title) throws DAOException;

    //public Boolean deleteNeo4j(String id) throws DAOException, NoSuchRecordException;

    //public Boolean updateNeo4j(String id, String newTitle) throws DAOException, NoSuchRecordException;

}
