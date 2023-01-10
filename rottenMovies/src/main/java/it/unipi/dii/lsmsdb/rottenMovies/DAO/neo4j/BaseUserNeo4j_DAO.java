package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.*;

import static org.neo4j.driver.Values.parameters;

/**
 * <class>BaseUserNeo4j_DAO</class> allow to use methods to interact with the GraphDB specifically
 * for the User/TopCritic entities
 */
public class BaseUserNeo4j_DAO extends BaseNeo4jDAO implements BaseUserDAO {
    /**
     * <method>insert</method> create a new entry in the GraphDB
     *
     * @param usr is the BaseUser model to insert into the DB
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public ObjectId insert(BaseUser usr) throws DAOException {
        ObjectId newId = usr.getId();
        if(newId.toString().isEmpty() || usr.getUsername().isEmpty()){
            return null;
        }

        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
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
        return newId;
    }

    /**
     * <method>delete</method> removes an entry from the GraphDB and all of its relationship
     * @param usr is the BaseUser model used to get the info to retrieve and delete from the GraphDB
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean delete(BaseUser usr) throws DAOException{
        if(usr.getId().toString().isEmpty()){
            return  false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx ->{
            String query = "MATCH (b{id: $id}) " +
                    "DETACH DELETE b";
            Result result = tx.run(query, parameters("id", usr.getId().toString()));
            return 1;
        });
        return true;

    }


    public int getNumberOfFollowers(TopCritic topCritic) throws DAOException{
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        int numberOfFollowers = session.readTransaction((TransactionWork<Integer>) tx ->{
            String query = "MATCH (t:TopCritic{id:$id})<-[:FOLLOWS]-() " +
                    "RETURN count(*) as NumFollowers";
            Result result = tx.run(query, parameters("id", topCritic.getId().toString()));
            System.out.println(result.peek().get("NumMovies"));
            return result.single().get("NumFollowers").asInt();

        });

        return numberOfFollowers;
    }

    /**
     * <method>followTopCritic</method> create a relationship of type FOLLOWS between a user and a top critic
     * @param user is the non-top critic user that wants to follow a top critic
     * @param topCritic is the top critic user target of the follow operation
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean followTopCritic(BaseUser user, BaseUser topCritic) throws DAOException{
        if(!(user instanceof  User) || !(topCritic instanceof TopCritic) )
            return false;
        if(user.getId().toString().isEmpty() || topCritic.getId().toString().isEmpty()){
            return  false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{id: $userId}), " +
                            "(t:TopCritic{id: $topCriticId}) " +
                            "MERGE (u)-[f:FOLLOWS]->(t)" +
                            "RETURN type(f) as Type";
            Result result = tx.run(query, parameters("userId", user.getId().toString(),
                    "topCriticId", topCritic.getId().toString()));
            System.out.println(result.single().get("Type").asString());
            return 1;
        });
        return true;
    }

    /**
     * <method>unfollowTopCritic</method> eliminate a relationship of type FOLLOWS between a user and a top critic
     * @param user is the non-top critic user that wants to unfollow a top critic
     * @param topCritic is the top critic user target of the unfollow operation
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean unfollowTopCritic(BaseUser user, BaseUser topCritic) throws DAOException {
        if(!(user instanceof  User) || !(topCritic instanceof TopCritic) )
            return false;
        if(user.getId().toString().isEmpty() || topCritic.getId().toString().isEmpty()){
            return  false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MATCH (u:User{id: $userId})" +
                    "-[f:FOLLOWS]->"+
                    "(t:TopCritic{id: $topCriticId}) " +
                    "DELETE f";
            Result result = tx.run(query, parameters("userId", user.getId().toString(),
                    "topCriticId", topCritic.getId().toString()));
            return 1;
        });
        return true;
    }

    /**
     * <method>getFeed</method> generate a feed of the review made by a followed top critic user
     * @param usr is the non-top critic user that want to see the feed
     * @param page is the number of visualization page, is used to determine which review to return
     * @return a list of ReviewFeedDTO used to create a feed for the reviews made by top critic user
     * @throws DAOException
     */
    @Override
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException {
        if(usr.getId().toString().isEmpty() || page<0){
            return null;
        }
        int skip = page*REVIEWS_IN_FEED;

        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        ArrayList<ReviewFeedDTO> reviewFeed = session.readTransaction((TransactionWork<ArrayList<ReviewFeedDTO>>)(tx -> {
            String query = "MATCH(u:User{id:$userId})-[f:FOLLOWS]->(t:TopCritic)-[r:REVIEWED]->(m:Movie) "+
                    "RETURN t.id as Id, m.title AS movieTitle, m.id AS movieId, t.name AS criticName, r.date AS reviewDate, "+
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
                        r.get("Id").asString(),
                        r.get("movieTitle").asString(),
                        r.get("movieId").asString(),
                        r.get("criticName").asString(),
                        r.get("content").asString(),
                        r.get("freshness").asBoolean(),
                        date
                ));
            }
            return feed;
        }));
        return reviewFeed;
    }

    /**
     * <method>getSuggestion</method> generate feed to suggest to a base user some top critic that he/she doesn't follow
     * @param usr is the non-top critic user that want to see the feed
     * @param page is the number of visualization page, is used to determine which top critic suggestion to return
     * @return a list of TopCriticSuggestionDTO to visualize the suggested top critic
     * @throws DAOException
     */
    @Override
    public ArrayList<TopCriticSuggestionDTO> getSuggestion(User usr, int page) throws DAOException{
        if(usr.getId().toString().isEmpty() || page<0){
            return null;
        }
        int skip = page*SUGGESTIONS_IN_FEED;
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        ArrayList<TopCriticSuggestionDTO> suggestionFeed = session.readTransaction((TransactionWork<ArrayList<TopCriticSuggestionDTO>>)(tx -> {
            String query = "MATCH(u:User{id:$userId})-[r:REVIEWED]->(m:Movie)<-[r2:REVIEWED]-(t:TopCritic) "+
                    "WHERE NOT (u)-[:FOLLOWS]->(t) " +
                    "RETURN 100*(toFloat(sum(case when r.freshness = r2.freshness then 1 else 0 end)+1)/(count(m.title)+2)) as Rate, "+
                    "t.name as Name, t.id as Id ORDER BY Rate DESC SKIP $skip LIMIT $limit";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
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

    public boolean checkIfFollows(BaseUser user, BaseUser topCritic) throws DAOException{
        if(!(user instanceof  User) || !(topCritic instanceof TopCritic) )
            return false;
        if(user.getId().toString().isEmpty() ){
            return false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        boolean check = session.readTransaction(tx -> {
            String query = "MATCH (u:User{id: $userId}),(t:TopCritic{id:$topCriticId}) " +
                    "RETURN  EXISTS ((u)-[:FOLLOWS]->(t)) as Check";
            Result result = tx.run(query, parameters("userId", user.getId().toString(),
                    "topCriticId", topCritic.getId().toString()));
            return result.peek().get("Check").asBoolean();
        });
        return check;
    }

    @Override
    public void queryBuildExcludeBanned() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<RegisteredUserDTO> executeSearchQuery(int page) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public boolean update(BaseUser usr) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }


    @Override
    public void queryBuildSearchByUsername(String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByUsernameExact(String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByLastName(String lastname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByFirstName(String firstname) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByYearOfBirth(int year) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByRegistrationDate(int year,int month,int day) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public ArrayList<GenresLikeDTO> getMostReviewedGenres (String username) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchPasswordEquals(String password) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public boolean executeDeleteQuery() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public void queryBuildExcludeAdmin() throws DAOException{
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

}
