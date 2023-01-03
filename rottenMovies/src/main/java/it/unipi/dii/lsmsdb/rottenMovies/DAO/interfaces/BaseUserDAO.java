package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface BaseUserDAO extends AutoCloseable{
    void queryBuildSearchByUsername(String username) throws DAOException;
    void queryBuildSearchByUsernameExact(String username) throws DAOException;
    void queryBuildSearchByLastName(String lastname) throws DAOException;
    void queryBuildSearchByFirstName(String lastname) throws DAOException;
    void queryBuildSearchByYearOfBirth(int year) throws DAOException;
    void queryBuildSearchPasswordEquals(String password) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByRegistrationDate(int year,int month,int day) throws DAOException;
    boolean insert(BaseUser usr) throws DAOException;
    boolean update(BaseUser usr) throws DAOException;
    boolean delete(BaseUser usr) throws DAOException;
    void getMostReviewedGenres (ObjectId user_id) throws DAOException;
    ArrayList<BaseUserDTO> executeSearchQuery(int page) throws DAOException;
    boolean executeDeleteQuery() throws DAOException;
    ArrayList<UserDTO> getMostReviewUser() throws DAOException;
    TopCriticDTO getMostFollowedCritic() throws DAOException;
    boolean followTopCritic(BaseUser user, BaseUser topCritic) throws DAOException;
    boolean unfollowTopCritic(BaseUser user, BaseUser topCritic) throws DAOException;
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException;

    public ArrayList<TopCriticSuggestionDTO> getSuggestion(BaseUser usr, int page) throws DAOException;

    public boolean checkIfFollows(BaseUser user, BaseUser topCritic) throws DAOException;
}