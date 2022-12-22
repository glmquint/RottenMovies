package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.List;

public interface BaseUserDAO extends AutoCloseable{
    public BaseUser getUserByUserName(String name) throws DAOException;
    public List<BaseUser> getAllUsers() throws DAOException;
    public Boolean insertBaseUser(BaseUser usr) throws DAOException;
    public Boolean modifyBaseUser(BaseUser usr) throws DAOException;

    public BaseUser getMostReviewUser() throws DAOException;
}
