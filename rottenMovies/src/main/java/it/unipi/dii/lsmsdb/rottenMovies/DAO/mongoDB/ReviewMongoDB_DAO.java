package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.Date;

public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user){
        // TODO: implement this method
        // TODO: execute only if user.username == "[[IS_GOING_TO_BE_DELETED]]"
        return true;
    }

    public boolean reviewMovie(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

}
