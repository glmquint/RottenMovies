package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class UserService {
    public BaseUserDTO getUser(int page, String user_id) {
        BaseUserDTO user = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            userdao.queryBuildSearchById(new ObjectId(user_id));
            user = userdao.executeSearchQuery(0).get(0);
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
}
