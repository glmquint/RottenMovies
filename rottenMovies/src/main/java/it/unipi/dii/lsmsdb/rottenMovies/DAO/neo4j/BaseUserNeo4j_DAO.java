package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;

import java.util.List;

public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {

    public User getMostReviewUser() throws DAOException{
        Session session = driver.session();
        User user = new User();

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
    public User getByUsername(String name) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public List<BaseUser> getAll() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean insert(BaseUser usr) throws DAOException {
        // TODO: actually we need to insert new users in Neo4J too,
        //  need to switch
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public Boolean delete(BaseUser usr) {
        //TODO: implement the delete query in Neo4j
        return true;
    }

    @Override
    public Boolean modify(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public BaseUser getById(ObjectId id) throws DAOException{
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
}
