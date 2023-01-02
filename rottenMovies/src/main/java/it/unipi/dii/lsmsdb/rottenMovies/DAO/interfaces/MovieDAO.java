package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Fabio
 * <interface>MovieDao</interface> interface for the DAO of the movie
 */
public interface MovieDAO extends AutoCloseable {

    ArrayList<MovieDTO> executeSearchQuery(int page, SortOptions sort_opt, ReviewProjectionOptions proj_opt) throws DAOException;
    boolean executeDeleteQuery() throws DAOException;
    void queryBuildSearchByTitleExact(String title) throws DAOException;
    void queryBuildSearchByTitle(String title) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByYear(int year, boolean afterYear) throws DAOException;
    void queryBuildSearchByTopRatings(int rating, boolean type) throws DAOException;
    void queryBuildsearchByUserRatings(int rating, boolean type) throws DAOException;
    void queryBuildSearchPersonnel(String[] workers, boolean includeAll) throws DAOException;
    void queryBuildSearchGenres(String[] genres, boolean includeAll) throws DAOException;
    LinkedHashMap<String, HashMap<String, Double>> mostSuccesfullProductionHouses(int numberOfMovies) throws DAOException;
    LinkedHashMap<String, HashMap<String, Double>> mostSuccesfullGenres(int numberOfMovies) throws DAOException;
    boolean update(Movie updated) throws DAOException;
    boolean insert(Movie newOne) throws DAOException;
    boolean delete (Movie movie) throws DAOException;

    //public Boolean insertNeo4j(String id, String title) throws DAOException;

    //public Boolean deleteNeo4j(String id) throws DAOException, NoSuchRecordException;

    //public Boolean updateNeo4j(String id, String newTitle) throws DAOException, NoSuchRecordException;

}
