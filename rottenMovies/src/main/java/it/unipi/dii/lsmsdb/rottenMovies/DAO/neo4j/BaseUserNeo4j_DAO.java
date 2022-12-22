package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.List;

public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {

    public BaseUser getMostReviewUser() throws DAOException{
        Session session = driver.session();
        BaseUser user = new BaseUser();

        String mostReviewUser = session.readTransaction((TransactionWork<String>) tx ->{
            String query = "MATCH (u:User)-[:REVIEWED]->(m:Movie) " +
                    "RETURN u.name AS Name, count(*) as NumMovies " +
                    "ORDER BY NumMovies DESC " +
                    "LIMIT 1";
            Result result = tx.run(query);
            System.out.println(result.peek().get("NumMovies"));
            return result.single().get("Name").asString();

        });
        user.setUsername((mostReviewUser));
        return user;
    }
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
}
