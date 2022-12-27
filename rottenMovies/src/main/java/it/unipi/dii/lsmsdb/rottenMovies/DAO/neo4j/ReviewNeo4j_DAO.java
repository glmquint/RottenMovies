package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;

public class ReviewNeo4j_DAO extends BaseNeo4jDAO implements ReviewDAO {
    @Override
    public boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
}
