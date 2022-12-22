package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;

import java.util.List;

public class ReviewMongoDB_DAO extends BaseMongoDAO {
    public Boolean updateReviewsByDeletedBaseUser(List<SimplyfiedReview> reviews){
        // TODO: implement this method
        return true;
    }

}
