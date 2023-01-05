package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public interface ReviewDAO extends AutoCloseable {
    boolean reviewMovie(BaseUser usr, Review review) throws DAOException;
    boolean delete(Review review) throws DAOException;
    boolean update(BaseUser usr, Review review) throws DAOException;

    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws DAOException;


}
