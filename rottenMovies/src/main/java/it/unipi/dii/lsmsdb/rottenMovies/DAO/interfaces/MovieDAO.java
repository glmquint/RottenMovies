package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.HallOfFameDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.YearMonthReviewDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * <interface>MovieDAO</interface> is the base interface that offers the methods below
 *  to the MovieMongoDB_DAO and MovieNeo4j_DAO classes that implement them
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
    ArrayList<HallOfFameDTO> mostSuccesfullProductionHouses(int numberOfMovies, SortOptions opt) throws DAOException;
    ArrayList<HallOfFameDTO> mostSuccesfullGenres(int numberOfMovies, SortOptions opt) throws DAOException;
    ArrayList<HallOfFameDTO> bestYearsBasedOnRatings (int numberOfMovies, SortOptions opt) throws DAOException;
    ArrayList<YearMonthReviewDTO> getYearAndMonthReviewActivity(ObjectId id) throws DAOException;
    boolean update(Movie updated) throws DAOException;
    ObjectId insert(Movie newOne) throws DAOException;
    boolean delete (Movie movie) throws DAOException;

    //public Boolean insertNeo4j(String id, String title) throws DAOException;

    //public Boolean deleteNeo4j(String id) throws DAOException, NoSuchRecordException;

    //public Boolean updateNeo4j(String id, String newTitle) throws DAOException, NoSuchRecordException;

}
