package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface BaseUserDAO extends AutoCloseable{
    MongoCollection<Document> getCollection() throws DAOException;
    void queryBuildSearchByUsername(String username) throws DAOException;
    void queryBuildSearchByLastName(String lastname) throws DAOException;
    void queryBuildSearchByFirstName(String lastname) throws DAOException;
    void queryBuildSearchByYearOfBirth(int year) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByRegistrationDate(int year,int month,int day) throws DAOException;
    boolean insert(BaseUser usr) throws DAOException;
    boolean update(BaseUser usr) throws DAOException;
    boolean delete(BaseUser usr) throws DAOException;
    ArrayList<BaseUserDTO> executeSearchQuery(int page) throws DAOException;
    boolean executeDeleteQuery() throws DAOException;
    BaseUserDTO getMostReviewUser() throws DAOException;
    TopCriticDTO getMostFollowedCritic() throws DAOException;
    boolean followTopCritic(String userName, String topCriticName) throws DAOException;
    boolean unfollowTopCritic(String userName, String topCriticName) throws DAOException;

}