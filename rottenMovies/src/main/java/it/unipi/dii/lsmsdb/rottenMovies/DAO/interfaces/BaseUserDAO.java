package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import org.bson.types.ObjectId;

import java.util.ArrayList;
/**
 * <interface>BaseUserDAO</interface> is the base interface that offers the methods below
 *  to the BaseUserMongoDB_DAO and BaseUserNeo4j_DAO classes that implement them
 */
public interface BaseUserDAO extends AutoCloseable{
    void queryBuildSearchByUsername(String username) throws DAOException;
    void queryBuildSearchByUsernameExact(String username) throws DAOException;
    void queryBuildSearchByLastName(String lastname) throws DAOException;
    void queryBuildSearchByFirstName(String lastname) throws DAOException;
    void queryBuildSearchByYearOfBirth(int year) throws DAOException;
    void queryBuildSearchPasswordEquals(String password) throws DAOException;
    void queryBuildSearchById(ObjectId id) throws DAOException;
    void queryBuildSearchByRegistrationDate(int year,int month,int day) throws DAOException;
    ObjectId insert(BaseUser usr) throws DAOException;
    boolean update(BaseUser usr) throws DAOException;
    boolean delete(BaseUser usr) throws DAOException;

    ArrayList<GenresLikeDTO> getMostReviewedGenres (String username) throws DAOException;

    ArrayList<RegisteredUserDTO> executeSearchQuery(int page) throws DAOException;

    boolean executeDeleteQuery() throws DAOException;
    boolean followTopCritic(BaseUser user, BaseUser topCritic) throws DAOException;
    boolean unfollowTopCritic(BaseUser user, BaseUser topCritic) throws DAOException;
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException;

    public ArrayList<TopCriticSuggestionDTO> getSuggestion(User usr, int page) throws DAOException;
    void queryBuildExcludeBanned() throws DAOException;
    public void queryBuildExcludeAdmin() throws DAOException;
    public boolean checkIfFollows(BaseUser user, BaseUser topCritic) throws DAOException;

    public int getNumberOfFollowers(TopCritic topCritic) throws DAOException;
}