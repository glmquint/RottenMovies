package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.List;

public interface BaseUserDAO extends AutoCloseable{
    public BaseUser getUserByUserName(String name);
    public List<BaseUser> getAllUsers();
    public Boolean insertBaseUser(BaseUser usr);
    public Boolean modifyBaseUser(BaseUser usr);
    public Boolean deleteBaseUser(String username);
}
