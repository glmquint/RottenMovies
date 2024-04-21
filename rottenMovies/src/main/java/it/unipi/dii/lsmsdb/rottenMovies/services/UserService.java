package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.utils.MD5;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <class>UserService</class> contains all the utility method to operate as middleware for the user entities
 */
public class UserService {

    /**
     * <method>getUser</method> get a user from the DB and returns it to the front-end
     * @param page is the page use for pagination
     * @param user_id is the id of the user to search for
     * @return a RegisteredUserDTO for checks and manipulation
     */
    public RegisteredUserDTO getUser(int page, String user_id) {
        RegisteredUserDTO user = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            userdao.queryBuildSearchById(new ObjectId(user_id));
            user = userdao.executeSearchQuery(page).get(0);
        } catch (Exception e) {
            System.err.println(e);
        }
        return user;
    }

    /**
     * <method>authenticate</method> allows to authenticate a user after the login
     * @param username is the username passed from the front-end
     * @param password is the password passed from the front-end
     * @return a RegisteredUserDTO for checks and manipulation
     */
    public RegisteredUserDTO authenticate(String username, String password) {
        ArrayList<RegisteredUserDTO> baseuserdtos = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)){
            userdao.queryBuildExcludeBanned();
            userdao.queryBuildSearchByUsernameExact(username);
            userdao.queryBuildSearchPasswordEquals(password);
            baseuserdtos = userdao.executeSearchQuery(0);
        } catch (Exception e){
            System.err.println(e);
        }
        if (baseuserdtos.isEmpty())
            return null;
        return baseuserdtos.get(0);
    }

    /**
     * <method>getGenresLike</method> builds a page for the most reviewed genres by a user
     * @param username is the username of the user to check for
     * @return a PageDTO of GenresLikeDTO for visualization
     */
    public PageDTO<GenresLikeDTO> getGenresLike (String username) {
        PageDTO<GenresLikeDTO> genresLikeDTO = new PageDTO<>();
        ArrayList<GenresLikeDTO> genresLikeDTOSpages = new ArrayList<>();
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            genresLikeDTOSpages = userdao.getMostReviewedGenres(username);
            genresLikeDTO.setEntries(genresLikeDTOSpages);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return genresLikeDTO;
    }

    /**
     * <method>register</method> allows the registration of a new user to be inserted in the DB
     * @param hm contains the information on the new user
     * @return a RegisteredUserDTO for checks and manipulation
     */
    public RegisteredUserDTO register(HashMap<String, String> hm) {
        BaseUser user = null;
        if (hm.containsKey("is_top_critic")) {
            user = new TopCritic();
        } else {
            user = new User();
        }
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v.isEmpty()) {
                continue;
            }
            if (k.equals("username")) {
                user.setUsername(v);
            } else if (k.equals("password")) {
                user.setPassword(MD5.getMd5(v));
            } else if (k.equals("first_name")) {
                user.setFirstName(v);
            } else if (k.equals("last_name")) {
                user.setLastName(v);
            } else if (k.equals("birthday") && user instanceof User) {
                ((User) user).setBirthdayDate(v);
            }
        }
        user.setRegistrationDate(new Date());
        ObjectId newId;
        try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            newId = userDAO.insert(user);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if (newId == null) {
            return null;
        }
        user.setId(newId);
        newId = null;
        try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)) {
            newId = userDAO.insert(user);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (newId == null) { // mongo roll-back
            try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
                userDAO.delete(user);
            } catch (Exception e) {
                System.err.println(e);
            }
            return null;
        }
        if (user instanceof TopCritic) {
            return new TopCriticDTO((TopCritic) user);
        }
        return new UserDTO((User) user);
    }

    /**
     * <method>getUserByUsername</method> get a user from the db by searching on its username
     * @param username is the username to search for
     * @return a RegisteredUserDTO for checks and manipulation
     */
    public RegisteredUserDTO getUserByUsername(String username) {
        RegisteredUserDTO user = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            userdao.queryBuildSearchByUsername(username);
            user = userdao.executeSearchQuery(0).get(0);
        } catch (Exception e) {
            System.err.println(e);
        }
        return user;
    }

    /**
     * <method>follow</method> allows a user to follow a top critic and it updates the db
     * @param uid is the id of the follower user
     * @param tid is the id of the followed top critic
     * @return true if the operation concluded successfully
     */
    public boolean follow (String uid, String tid){
        try(BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
            BaseUser user = new User();
            user.setId(new ObjectId(uid));
            BaseUser topCritic = new TopCritic();
            topCritic.setId(new ObjectId(tid));
            System.out.println(user.getId());
            System.out.println(topCritic.getId());
            if(!userDAO.checkIfFollows(user,topCritic)) {
                System.out.println(userDAO.followTopCritic(user, topCritic));
            }
            else{
                return false;
            }
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
        return true;
    }

    /**
     * <method>unfollow</method> allows a user to unfollow a top critic and it updates the db
     * @param uid is the id of the follower user
     * @param tid is the id of the followed top critic
     * @return true if the operation concluded successfully
     */
    public boolean unfollow (String uid, String tid){
        try(BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
            BaseUser user = new User();
            user.setId(new ObjectId(uid));
            BaseUser topCritic = new TopCritic();
            topCritic.setId(new ObjectId(tid));
            if(userDAO.checkIfFollows(user,topCritic)) {
                System.out.println(userDAO.unfollowTopCritic(user, topCritic));
            }
            else{
                return false;
            }
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
        return true;
    }

    /**
     * <method>createUserFeed</method> builds the page for the visualization of the feed
     * @param usr is model of the user for which the feed must be built
     * @param page is the page use for pagination
     * @return a pageDTO of ReviewFeedDTO for visualization
     */
    public PageDTO<ReviewFeedDTO> createUserFeed (BaseUser usr,int page){
        PageDTO<ReviewFeedDTO> reviewFeedDTO = new PageDTO<>();
        ArrayList<ReviewFeedDTO> reviewFeedDTOpages = new ArrayList<>();
        try(BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
            reviewFeedDTOpages=userDAO.getFeed(usr,page);
            reviewFeedDTO.setEntries(reviewFeedDTOpages);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return reviewFeedDTO;
    }

    /**
     * <method>getTopCriticSuggestions</method> build the page for the visualization of the suggestion feed
     * @param usr is model of the user for which the feed must be built
     * @param page is the page use for pagination
     * @return a pageDTO of TopCriticSuggestionDTO for visualization
     */
    public PageDTO<TopCriticSuggestionDTO> getTopCriticSuggestions (User usr, int page) {
        PageDTO<TopCriticSuggestionDTO> topCriticSuggestionDTO = new PageDTO<>();
        ArrayList<TopCriticSuggestionDTO> topCriticSuggestionPages = new ArrayList<>();
        try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)) {
            topCriticSuggestionPages = userDAO.getSuggestion(usr, page);
            topCriticSuggestionDTO.setEntries(topCriticSuggestionPages);
        } catch (Exception e) {
            System.out.println(e);
        }
        return topCriticSuggestionDTO;
    }

    /**
     * <method>getFollowers</method> get the number of followers of a top critic from the db
     * @param id is the id of the top critic
     * @return the number of followers
     */
    public int getFollowers(String id){
        TopCritic topCritic = new TopCritic();
        topCritic.setId(new ObjectId(id));
        try(BaseUserDAO baseUserDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
            return baseUserDAO.getNumberOfFollowers(topCritic);
        }catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

    /**
     * <method>getReviewIndex</method> get the position of a review of user in the review array of a user stored in the
     * db
     * @param userid is the id of the author of the review
     * @param primaryTitle is the title of the movie
     * @return an ArrayList with the index of the review and the id of the movie
     */
    public ArrayList<Object> getReviewIndex(ObjectId userid, String primaryTitle){
        try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.MONGO)) {
            return reviewdao.getIndexOfReview(userid,primaryTitle);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * <method>modifyUser</method> allows the modification of a user details with data passed from the front-end and
     * updates the db
     * @param uid is the id of the user
     * @param hm contains the data to be modified
     * @param isTop indicates if the user is a top critic or a normal user
     * @return true if the operation concluded successfully
     */
    public boolean modifyUser(String uid, HashMap<String, String> hm, boolean isTop) {
        BaseUser newUser = null;
        if(!isTop){
            newUser = new User();
        }
        else{
            newUser = new TopCritic();
        }
        newUser.setId(new ObjectId(uid));
        newUser.setPassword("");
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v == null || v.isEmpty()) {
                continue;
            }
            if (k.equals("firstName")) {
                newUser.setFirstName(v);
            } else if (k.equals("lastName")) {
                newUser.setLastName(v);
            }
            else if (k.equals("password")) {
                newUser.setPassword(MD5.getMd5(v));
            }
            else if (k.equals("birthday")) {
                ((User) newUser).setBirthdayDate(v);
            }
        }

        try(BaseUserDAO baseUserDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)){
            return baseUserDAO.update(newUser);
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }
}
