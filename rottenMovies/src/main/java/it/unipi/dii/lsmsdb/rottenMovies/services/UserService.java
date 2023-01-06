package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptionsEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.utils.MD5;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {
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
        boolean res;
        try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            res = userDAO.insert(user);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if (!res) {
            return null;
        }
        res = false;
        try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)) {
            res = userDAO.insert(user);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (!res) {
            try (BaseUserDAO userDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
                res = userDAO.delete(user);
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
}
