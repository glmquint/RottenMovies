package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.types.ObjectId;

import java.util.ArrayList;
/**
 * <interface>ReviewDAO</interface> is the base interface that offers the methods below
 *  to the ReviewMongoDB_DAO and ReviewNeo4j_DAO classes that implement them
 */
public interface ReviewDAO extends AutoCloseable {
    boolean reviewMovie(BaseUser usr, Review review) throws DAOException;
    boolean delete(Review review) throws DAOException;
    boolean update(BaseUser usr, Review review) throws DAOException;
    ArrayList<Object> getIndexOfReview(ObjectId userid, String primaryTitle) throws DAOException;
    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws DAOException;


}
