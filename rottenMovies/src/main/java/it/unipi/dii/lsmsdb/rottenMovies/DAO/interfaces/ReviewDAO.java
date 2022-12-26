package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.Date;
import java.util.List;

public interface ReviewDAO extends AutoCloseable {
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException;
    public boolean reviewMovieNeo4j(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException;

    public boolean deleteReviewNeo4j(String userId, String movieId) throws DAOException;

    public List<ReviewFeedDTO> constructFeedNeo4j(String userId, int page) throws DAOException;
    //Boolean reviewMovieNeo4j(ReviewDTO review, MovieDTO movie);
}
