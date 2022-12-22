package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;

import java.util.List;

public interface ReviewDAO extends AutoCloseable {
    public Boolean updateReviewsByDeletedBaseUser(List<SimplyfiedReview> reviews) throws DAOException;

    //Boolean reviewMovie(ReviewDTO review, MovieDTO movie);
}
