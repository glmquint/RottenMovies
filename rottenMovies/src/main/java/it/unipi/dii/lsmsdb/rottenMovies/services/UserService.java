package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptionsEnum;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptionsEnum;
import org.bson.types.ObjectId;

public class UserService {
    public BaseUserDTO getUser(int page, String user_id) {
        BaseUserDTO user = null;
        try (BaseUserDAO userdao = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            userdao.queryBuildSearchById(new ObjectId(user_id));
            user = userdao.executeSearchQuery(0).get(0);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return user;
    }
}
