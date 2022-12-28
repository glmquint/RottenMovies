package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
import org.bson.Document;
import org.neo4j.driver.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.*;

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

    public boolean insert(BaseUser usr) throws DAOException {
        if(usr.getId().toString().isEmpty() || usr.getUsername().isEmpty()){
            return  false;
        }

        Session session = driver.session();
        String newUserName = session.writeTransaction((TransactionWork<String>)  tx ->{
            String query;
            if(usr instanceof TopCritic){
                query = "MERGE (t:TopCritic{id: $id}) "+
                        "ON CREATE SET t.id = $id, t.name= $name "+
                        "RETURN t.name as Name";
            }
            else{
                query = "MERGE (u:User{id: $id}) "+
                        "ON CREATE SET u.id=$id, u.name= $name "+
                        "RETURN u.name as Name";
            }

            Result result = tx.run(query, parameters("id", usr.getId().toString(), "name", usr.getUsername()));
            return result.single().get("Name").asString();
        });
        System.out.println(newUserName);
        return true;
    }

    public boolean delete(BaseUser usr) throws DAOException{
        if(usr.getId().toString().isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx ->{
            String query = "MATCH (b{id: $id}) " +
                    "DETACH DELETE b";
            Result result = tx.run(query, parameters("id", usr.getId().toString()));
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
    public boolean followTopCritic(BaseUser user, BaseUser topCritic) throws DAOException{
        if(!(user instanceof  User) || !(topCritic instanceof TopCritic) )
            return false;
        if(user.getUsername().isEmpty() || topCritic.getUsername().isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{name: $userName}), " +
                            "(t:TopCritic{name: $topCriticName}) " +
                            "MERGE (u)-[f:FOLLOWS]->(t)" +
                            "RETURN type(f) as Type";
            Result result = tx.run(query, parameters("userName", user.getUsername(),
                    "topCriticName", topCritic.getUsername()));
            System.out.println(result.peek().get("Type").asString());
            return 1;
        });
        return true;
    }

    /*MATCH (n {name: 'Andy'})-[r:KNOWS]->()
    DELETE r*/

    public boolean unfollowTopCritic(BaseUser user, BaseUser topCritic) throws DAOException {
        if(!(user instanceof  User) || !(topCritic instanceof TopCritic) )
            return false;
        if(user.getUsername().isEmpty() || topCritic.getUsername().isEmpty()){
            return  false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{name: $userName})" +
                    "-[f:FOLLOWS]->"+
                    "(t:TopCritic{name: $topCriticName}) " +
                    "DELETE f";
            Result result = tx.run(query, parameters("userName", user.getUsername(),
                    "topCriticName", topCritic.getUsername()));
            return 1;
        });
        return true;
    }

    @Override
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException {
        if(usr.getId().toString().isEmpty() || page<0){
            return null;
        }
        int skip = page*REVIEWS_IN_FEED;

        Session session = driver.session();
        ArrayList<ReviewFeedDTO> reviewFeed = session.readTransaction((TransactionWork<ArrayList<ReviewFeedDTO>>)(tx -> {
            String query = "MATCH(u:User{id:$userId})-[f:FOLLOWS]->(t:TopCritic)-[r:REVIEWED]->(m:Movie) "+
                    "RETURN m.title AS movieTitle,t.name AS criticName, r.date AS reviewDate, "+
                    "r.content AS content, r.freshness AS freshness " +
                    "ORDER BY r.date DESC SKIP $skip LIMIT $limit ";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
                    "skip", skip, "limit", REVIEWS_IN_FEED));
            ArrayList<ReviewFeedDTO> feed = new ArrayList<>();
            while(result.hasNext()){
                Record r = result.next();
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(r.get("reviewDate").asLocalDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                feed.add(new ReviewFeedDTO(
                        r.get("movieTitle").asString(),
                        r.get("criticName").asString(),
                        r.get("content").asString(),
                        r.get("freshness").asBoolean(),
                        date
                ));
            }
            return feed;
        }));
        return reviewFeed;
        /*
        ArrayList<ReviewFeedDTO> reviewList = new ArrayList<>();
        try{
            reviewList = constructFeedNeo4j(usr.getId().toString(), page);
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return reviewList;

         */
    }

    /*
    MATCH (u:User{name:"Dennis Schwartz"})-[r:REVIEWED]->(m:Movie)<-[r2:REVIEWED]-(t:TopCritic)
    WHERE NOT (u)-[:FOLLOWS]->(t)
    RETURN 100*toFloat( sum(case when r.freshness = r2.freshness then 1 else 0 end)+1)/(count(m.title)+2) as perc,
    t.name as name ORDER by perc DESC LIMIT 20
     */

    public ArrayList<TopCriticSuggestionDTO> getSuggestion(BaseUser usr, int page) throws DAOException{
        if(usr.getId().toString().isEmpty() || page<0){
            return null;
        }
        int skip = page*SUGGESTIONS_IN_FEED;
        Session session = driver.session();
        ArrayList<TopCriticSuggestionDTO> suggestionFeed = session.readTransaction((TransactionWork<ArrayList<TopCriticSuggestionDTO>>)(tx -> {
            String query = "MATCH(u:User{name:$name})-[r:REVIEWED]->(m:Movie)<-[r2:REVIEWED]-(t:TopCritic) "+
                    "WHERE NOT (u)-[:FOLLOWS]->(t) " +
                    "RETURN 100*(toFloat(sum(case when r.freshness = r2.freshness then 1 else 0 end)+1)/(count(m.title)+2)) as Rate, "+
                    "t.name as Name, t.id as Id ORDER BY Rate DESC SKIP $skip LIMIT $limit";
            Result result = tx.run(query, parameters("name", usr.getUsername(),
                    "skip", skip, "limit", SUGGESTIONS_IN_FEED));
            ArrayList<TopCriticSuggestionDTO> feed = new ArrayList<>();
            while(result.hasNext()){
                Record r = result.next();

                feed.add(new TopCriticSuggestionDTO(
                        r.get("Id").asString(),
                        r.get("Name").asString(),
                        (int) r.get("Rate").asDouble()
                ));
            }
            return feed;
        }));
        return suggestionFeed;

    }

    @Override
    public ArrayList<BaseUserDTO> executeSearchQuery(int page) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public boolean update(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }


    public MongoCollection<Document> getCollection() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public void queryBuildSearchByUsername(String username) throws DAOException {
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
    public void queryBuildSearchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public boolean executeDeleteQuery() throws DAOException {
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
