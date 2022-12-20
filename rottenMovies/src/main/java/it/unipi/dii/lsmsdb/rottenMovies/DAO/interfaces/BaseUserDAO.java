package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;

import java.util.List;

public interface BaseUserDAO {
    public BaseUser getUserByUserName(String name);
    public List<BaseUser> getUser();
    public Boolean insertBaseUser(BaseUser usr);
    public Boolean modifyBaseUser(BaseUser usr);
}
