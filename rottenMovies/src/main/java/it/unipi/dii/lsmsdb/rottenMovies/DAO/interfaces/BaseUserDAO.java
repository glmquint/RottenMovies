package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import org.bson.types.ObjectId;

import java.util.List;

public interface BaseUserDAO extends AutoCloseable{
    public BaseUser getByUsername(String name) throws DAOException;
    public BaseUser getById(ObjectId id) throws DAOException;
    public List<BaseUser> getAll() throws DAOException;
    public Boolean insert(BaseUser usr) throws DAOException;
    public Boolean modify(BaseUser usr) throws DAOException;
    public Boolean delete(BaseUser usr) throws DAOException;
    public BaseUser getMostReviewUser() throws DAOException;
    public TopCritic getMostFollowedCritic() throws DAOException;
}
