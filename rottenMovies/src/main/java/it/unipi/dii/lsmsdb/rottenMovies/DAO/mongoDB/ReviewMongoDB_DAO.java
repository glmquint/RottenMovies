package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;

public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {
    public boolean updateReviewsByDeletedBaseUser(BaseUser user){
        // TODO: implement this method
        if(user.getFirstName()!= Constants.USERS_MARKED_AS_DELETED)
            return false;

        return true;
    }


}
