package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {
    public boolean updateReviewsByDeletedBaseUser(BaseUser user){
        // TODO: implement this method
        if(user.getFirstName()!= Constants.USERS_MARKED_AS_DELETED)
            return false;

        return true;
    }

    @Override
    public boolean reviewMovie(BaseUser usr, Review review) throws DAOException{
        throw new DAOException("method not implemented for Mongo DB");
    }

    @Override
    public boolean delete(Review review)throws DAOException{
        throw new DAOException("method not implemented for Mongo DB");
    }



    /*
    ==========================================================================
    INSERIRE SOPRA I METODI DI MONGO E SOTTO QUELLI DI NEO4J
    ==========================================================================
     */
    public boolean reviewMovieNeo4j(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean deleteReviewNeo4j(String userId, String movieId) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public MovieReviewBombingDTO checkReviewBombing(Movie movie, LocalDate date) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
