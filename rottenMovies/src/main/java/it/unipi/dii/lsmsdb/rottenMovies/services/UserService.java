package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class UserService {
    public BaseUserDTO getUser(int page, String user_id) {
        BaseUserDTO user = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            userdao.queryBuildSearchById(new ObjectId(user_id));
            user = userdao.executeSearchQuery(page).get(0);
        } catch (Exception e) {
            System.err.println(e);
        }
        return user;
    }

    public BaseUserDTO authenticate(String username, String password) {
        ArrayList<BaseUserDTO> baseuserdtos = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)){
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
}
