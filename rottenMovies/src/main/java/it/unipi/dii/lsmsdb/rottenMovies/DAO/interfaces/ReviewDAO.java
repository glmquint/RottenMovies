package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

public interface ReviewDAO extends AutoCloseable {
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException;

    //Boolean reviewMovie(ReviewDTO review, MovieDTO movie);
}
