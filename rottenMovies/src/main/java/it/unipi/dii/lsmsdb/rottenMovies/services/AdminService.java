package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;

public class AdminService {

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
}
