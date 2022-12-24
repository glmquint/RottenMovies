package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {
    public boolean updateReviewsByDeletedBaseUser(BaseUser user){
        // TODO: implement this method
        // TODO: execute only if user.username == "[[IS_GOING_TO_BE_DELETED]]"
        return true;
    }

}
