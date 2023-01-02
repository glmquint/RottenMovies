package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
import org.bson.Document;

import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {

    public BaseUserDTO getMostReviewUser() throws DAOException {
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
        return new UserDTO(user);
    }

    public TopCriticDTO getMostFollowedCritic() throws DAOException{
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
        return new TopCriticDTO(topCritic);
    }

    private boolean createBaseUser(String id, String name, boolean isTop) throws DAOException{
        if(id.isEmpty() || name.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        String newUserName = session.writeTransaction((TransactionWork<String>)  tx ->{
            String query;
            if(isTop){
                query = "MERGE (t:TopCritic{id: $id}) "+
                        "ON CREATE SET t.id = $id, t.name= $name "+
                        "RETURN t.name as Name";
            }
            else{
                query = "MERGE (u:User{id: $id}) "+
                        "ON CREATE SET u.id=$id, u.name= $name "+
                        "RETURN u.name as Name";
            }

            Result result = tx.run(query, parameters("id", id, "name", name));
            return result.single().get("Name").asString();
        });
        System.out.println(newUserName);
        return true;
    }

    private boolean deleteBaseUser(String id) throws DAOException{
        if(id.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx ->{
            String query = "MATCH (b{id: $id}) " +
                        "DETACH DELETE b";
            Result result = tx.run(query, parameters("id", id));
            return 1;
        });
        return true;
    }

    private boolean updateBaseUser(String id, String newName) throws DAOException{
        if(id.isEmpty() || newName.isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $id}) " +
                    "SET b.name=$newName RETURN b.name AS Name";

            Result result = tx.run(query, parameters("id", id, "newName", newName));
            System.out.println(result.single().get("Name").asString());
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
    public void queryBuildSearchByUsernameExact(String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByLastName(String lastname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByFirstName(String firstname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByYearOfBirth(int year) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByRegistrationDate(int year,int month,int day) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void getMostReviewedGenres (ObjectId user_id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchPasswordEquals(String password) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean executeDeleteQuery() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean insert(BaseUser usr) throws DAOException {
        try{
            createBaseUser(usr.getId().toString(), usr.getUsername(), usr instanceof TopCritic);
        } catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;
    }
    public boolean update(BaseUser usr) throws DAOException {
        try{
            updateBaseUser(usr.getId().toString(), usr.getUsername());
        } catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;
    }

    public boolean delete(BaseUser usr) throws DAOException{
        try{
            deleteBaseUser(usr.getId().toString());
        } catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;
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
