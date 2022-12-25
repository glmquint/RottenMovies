package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
import org.bson.Document;
import java.util.ArrayList;


import static org.neo4j.driver.Values.parameters;

public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {

    public User getMostReviewUser() throws DAOException {
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

    public TopCritic getMostFollowedCritic() throws DAOException{
        Session session = driver.session();
        TopCritic topCritic = new TopCritic();
        String mostFollowedCritic = session.readTransaction((TransactionWork<String>) tx ->{
            String query = "MATCH (t:TopCritic)<-[:FOLLOWS]-(u:User) " +
                    "RETURN t.name AS Name, count(*) as NumMovies " +
                    "ORDER BY NumMovies DESC " +
                    "LIMIT 1";
            Result result = tx.run(query);
            System.out.println(result.peek().get("NumMovies"));
            return result.single().get("Name").asString();

        });
        topCritic.setUsername((mostFollowedCritic));
        return topCritic;
    }

    public boolean createBaseUser(String name, boolean isTop) throws DAOException{
        if(name.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        String newUserName = session.writeTransaction((TransactionWork<String>)  tx ->{
            String query;
            if(isTop){
                query = "MERGE (t:TopCritic{name: $name}) "+
                        "ON CREATE SET t.name= $name "+
                        "RETURN t.name as Name";
            }
            else{
                query = "MERGE (u:User{name: $name}) "+
                        "ON CREATE SET u.name= $name "+
                        "RETURN u.name as Name";
            }

            Result result = tx.run(query, parameters("name", name));
            return result.single().get("Name").asString();
        });
        System.out.println(newUserName);
        return true;
    }

    public boolean deleteBaseUser(String name, boolean isTop) throws DAOException{
        if(name.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx ->{
            String query;
            if(isTop){
                query = "MATCH (t:TopCritic{name: $name}) " +
                            "DETACH DELETE t";
            }
            else{
                query = "MATCH (u:User{name: $name}) " +
                        "DETACH DELETE u";
            }

            Result result = tx.run(query, parameters("name", name));
            return 1;
        });
        return true;
    }

    public boolean followTopCritic(String userName, String topCriticName) throws DAOException{
        if(userName.isEmpty() || topCriticName.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{name: $userName}), " +
                            "(t:TopCritic{name: $topCriticName}) " +
                            "MERGE (u)-[f:FOLLOWS]->(t)" +
                            "RETURN type(f) as Type";
            Result result = tx.run(query, parameters("userName", userName, "topCriticName", topCriticName));
            System.out.println(result.single().get("Type").asString());
            return 1;
        });
        return true;
    }

    /*MATCH (n {name: 'Andy'})-[r:KNOWS]->()
    DELETE r*/

    public boolean unfollowTopCritic(String userName, String topCriticName) throws DAOException {
        if (userName.isEmpty() || topCriticName.isEmpty()) {
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{name: $userName})" +
                    "-[f:FOLLOWS]->"+
                    "(t:TopCritic{name: $topCriticName}) " +
                    "DELETE f";
            Result result = tx.run(query, parameters("userName", userName, "topCriticName", topCriticName));
            return 1;
        });
        return true;
    }
    public MongoCollection<Document> getCollection() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public ArrayList<BaseUserDTO> executeSearchQuery(int page) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByUsername(String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByUsernameContains(String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByLastNameContains(String lastname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByFirstNameContains(String firstname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean executeDeleteQuery() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean insert(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean update(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    /*
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

     */
}
