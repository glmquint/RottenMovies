package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

import java.util.List;

public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {
    @Override
    public BaseUser getUserByUserName(String name) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public List<BaseUser> getAllUsers() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean insertBaseUser(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean modifyBaseUser(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public BaseUser getMostReviewUser() throws DAOException{
        return null;
    }
}
