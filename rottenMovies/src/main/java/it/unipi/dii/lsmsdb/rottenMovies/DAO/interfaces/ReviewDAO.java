package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.Date;

public interface ReviewDAO extends AutoCloseable {
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException;
    public boolean reviewMovie(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException;

    public boolean deleteReviewNeo4j(String userId, String movieId) throws DAOException;
    //Boolean reviewMovie(ReviewDTO review, MovieDTO movie);
}
