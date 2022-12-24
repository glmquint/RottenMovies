package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface BaseUserDAO extends AutoCloseable{
    MongoCollection<Document> getCollection() throws DAOException;
    ArrayList<BaseUserDTO> executeSearchQuery(int page) throws DAOException;
    void queryBuildSearchByUsername(String username) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    boolean insert(BaseUser usr) throws DAOException;
    boolean update(BaseUser usr) throws DAOException;
    boolean executeDeleteQuery() throws DAOException;
    BaseUser getMostReviewUser() throws DAOException;
    TopCritic getMostFollowedCritic() throws DAOException;
    boolean createBaseUser(String name, boolean isTop) throws DAOException;
    boolean deleteBaseUser(String name, boolean isTop) throws DAOException;
    boolean followTopCritic(String userName, String topCriticName) throws DAOException;
    boolean unfollowTopCritic(String userName, String topCriticName) throws DAOException;

}