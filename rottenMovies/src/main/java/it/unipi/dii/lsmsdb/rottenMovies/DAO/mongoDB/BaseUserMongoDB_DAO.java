package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.*;


/**
 * <class>BaseUserMongoDB_DAO</class> queries the DB for BaseUser
 *  * based on different parameters like:
 *      - UserName
 *      - isTopCritic
 *      - number of reviews
 */
public class BaseUserMongoDB_DAO extends BaseMongoDAO implements BaseUserDAO {

    public BaseUserMongoDB_DAO() {
        super();
    }

    /**
     * <method>executeSearchQuery</method> executes a serch query in user collection
     * @param page is the page number to limit the result set
     * @return an Arraylist of RegisteredUserDTO
     * @throws DAOException
     */
    public ArrayList<RegisteredUserDTO> executeSearchQuery(int page){
        MongoCollection<Document>  collection = getUserCollection();
        ObjectMapper mapper = new ObjectMapper();
        FindIterable<Document> found = collection.find(query);
        if (page >= 0) { // only internally. Never return all users without pagination on front-end
            query=null;
            found = found.skip(page * Constants.USERS_PER_PAGE).limit(Constants.USERS_PER_PAGE);
        }
        MongoCursor<Document> cursor = found.iterator();
        ArrayList<RegisteredUserDTO> user_list = new ArrayList<>();
        RegisteredUser registeredUser;
        String json_user;
        Document doc;
        while(cursor.hasNext()){
            doc = cursor.next();
            json_user = doc.toJson();
            try {
                if(doc.containsKey("isAdmin")){
                    registeredUser = mapper.readValue(json_user, Admin.class);
                    user_list.add(new AdminDTO((Admin) registeredUser));
                }
                else if (doc.containsKey("date_of_birth")) { // it is a User
                    registeredUser = mapper.readValue(json_user, User.class);
                    user_list.add(new UserDTO((User) registeredUser));
                } else { // it is a Top Critic
                    registeredUser = mapper.readValue(json_user, TopCritic.class);
                    user_list.add(new TopCriticDTO((TopCritic) registeredUser));
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return user_list;
    }
    /**
     * <method>queryBuildSearchByUsername</method> builds a new query for finding a user by his username if none exists
     * or keeps concatenating the new query to the previous one
     * @param username
     * @throws DAOException
     */
    public void queryBuildSearchByUsername(String username){
        Bson new_query = Filters.regex("username", username, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    /**
     * <method>queryBuildSearchByFirstName</method> builds a new query for finding a user by his first name if none exists
     * or keeps concatenating the new query to the previous one
     * @param firstname
     * @throws DAOException
     */
    public void queryBuildSearchByFirstName(String firstname){
        Bson new_query = Filters.regex("first_name", firstname, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    /**
     * <method>queryBuildSearchByLastName</method> builds a new query for finding a user by his last name if none exists
     * or keeps concatenating the new query to the previous one
     * @param lastname
     * @throws DAOException
     */
    public void queryBuildSearchByLastName(String lastname){
        Bson new_query = Filters.regex("last_name", lastname, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchByYearOfBirth</method> builds a new query for finding a user by his year of birth if none exists
     * or keeps concatenating the new query to the previous one
     * @param year
     * @throws DAOException
     */
    public void queryBuildSearchByYearOfBirth(int year){
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 00, 00, 00);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        Bson new_query = Filters.and(Filters.gte("date_of_birth",start),Filters.lte("date_of_birth",end));
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchByRegistrationDate</method> builds a new query for finding a user by his registration date if none exists
     * or keeps concatenating the new query to the previous one
     * @param year,month,day
     * @throws DAOException
     */
    public void queryBuildSearchByRegistrationDate(int year,int month,int day){
        LocalDateTime start = LocalDateTime.of(year, month, day, 00, 00, 00);
        LocalDateTime end = LocalDateTime.of(year, month, day, 23, 59, 59);
        Bson new_query = Filters.and(Filters.gte("registration_date",start),Filters.lte("registration_date",end));
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    /**
     * <method>queryBuildSearchById</method> builds a new query for finding a user by his id if none exists
     * or keeps concatenating the new query to the previous one
     * @param id
     * @throws DAOException
     */
    public void queryBuildSearchById(ObjectId id){
        Bson new_query = Filters.eq("_id", id);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchByUsernameExact</method> builds a new query for finding a user by his exact username if none exists
     * or keeps concatenating the new query to the previous one
     * @param username
     * @throws DAOException
     */
    public void queryBuildSearchByUsernameExact(String username){
        Bson new_query = Filters.eq("username", username);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchPasswordEquals</method> builds a new query for finding a user by his password if none exists
     * or keeps concatenating the new query to the previous one
     * @param password
     * @throws DAOException
     */
    public void queryBuildSearchPasswordEquals(String password){
        Bson new_query = Filters.eq("password", password);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchPasswordEquals</method> builds a new query for excluding a banned user if none exists
     * or keeps concatenating the new query to the previous one
     * @throws DAOException
     */
    @Override
    public void queryBuildExcludeBanned() throws DAOException {
        Bson new_query = Filters.exists("isBanned", false);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildExcludeAdmin</method> builds a new query for excluding an admin user if none exists
     * or keeps concatenating the new query to the previous one
     * @throws DAOException
     */
    @Override
    public void queryBuildExcludeAdmin() throws DAOException {
        Bson new_query = Filters.ne("username", "admin");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>insert</method> is responsable for inserting a new user inside the collection
     * @param usr is the model representing a BaseUser
     * @return the ObjectID of the inserted user
     * @throws DAOException
     */
    public ObjectId insert(BaseUser usr){
        MongoCollection<Document>  collection = getUserCollection();
        ObjectId newId = new ObjectId();
        try {
            Document newdoc = new Document()
                    .append("_id", newId)
                    .append("username", usr.getUsername())
                    .append("password", usr.getPassword())
                    .append("first_name", usr.getFirstName())
                    .append("last_name", usr.getLastName())
                    .append("registration_date", usr.getRegistrationDate())
                    .append("last_3_reviews", new ArrayList<BasicDBObject>())
                    .append("reviews", new ArrayList<BasicDBObject>());
            if (usr instanceof User){
                    newdoc.append("date_of_birth", ((User)usr).getBirthdayDate());
            }
            InsertOneResult result = collection.insertOne(newdoc);
            System.out.println("Success! Inserted document id: " + result.getInsertedId());
        }
        catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
            return null;
        }
        return newId;
    }

    /**
     * <method>update</method> is responsible for updating user information inside the collection
     * @param usr is the model representing a BaseUser
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
    public boolean update(BaseUser usr) throws DAOException{
        MongoCollection<Document>  collection = getUserCollection();
        boolean returnvalue=true;
        Bson updates = Updates.combine(
                    Updates.set("first_name", usr.getFirstName()),
                    Updates.set("last_name", usr.getLastName()));
        if(!usr.getPassword().isEmpty()){
            updates = Updates.combine(updates, Updates.set("password", usr.getPassword()));
        }
        if (usr instanceof User){
            updates = Updates.combine(updates, Updates.set("date_of_birth", ((User)usr).getBirthdayDate()));
        }
        try {
            query=null;
            queryBuildSearchById(usr.getId());
            UpdateResult result = collection.updateOne(query, updates);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
    }

    /**
     * <method>delete</method> is responsable for deleting a specific user inside the collection
     * @param usr is the model representing a BaseUser
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
    public boolean delete(BaseUser usr) throws DAOException{
        queryBuildSearchById(usr.getId());
        boolean result = true;
        try{
            executeDeleteQuery();
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        query = null;
        return result;
    }

    /**
     * <method>executeDeleteQuery</method> is responsable for deleting users inside the collection
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
    public boolean executeDeleteQuery() {
        ArrayList<RegisteredUserDTO> users_to_delete = executeSearchQuery(-1);
        MongoCollection<Document>  collectionUser = getUserCollection();
        MongoCollection<Document>  collectionMovie = getMovieCollection();
        boolean returnvalue=true;
        ArrayList<SimplyfiedReviewDTO> listrev;
        Document set;
        for(RegisteredUserDTO b:users_to_delete){ // now I set the critic_name in the review field of movie as DELETED_USER
            if(b instanceof BaseUserDTO) {
                listrev = ((BaseUserDTO) b).getReviews();
                for (SimplyfiedReviewDTO s : listrev) {
                    set = new Document("$set", new Document("review." + s.getIndex() + ".critic_name", Constants.DELETED_USER));
                    collectionMovie.updateOne(new Document("_id", s.getMovieID()), set);
                }
            }
        }
        try { // now I delete the users from collection user
            DeleteResult result = collectionUser.deleteMany(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;

    }
    /**
     * <method>getMostReviewedGenres</method> implement the aggregation for finding most reviewed/liked genres
     * @param username is the username of the user
     * @return an ArrayList of GenresLikeDTO
     * @throws DAOException
     */
    public ArrayList<GenresLikeDTO> getMostReviewedGenres (String username){
        MongoCollection<Document>  collectionMovie = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collectionMovie.aggregate(
                Arrays.asList(
                        Aggregates.match(eq("review.critic_name",username)),
                        Aggregates.unwind("$genres"),
                        Aggregates.group("$genres",
                                sum("count",1)),
                        Aggregates.sort(Sorts.descending("count")),
                        Aggregates.limit(Constants.HALL_OF_FAME_ELEMENT_NUMBERS)
                )
        );
        ArrayList<GenresLikeDTO> resultSet = new ArrayList<>();
        GenresLikeDTO genresLikeDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            genresLikeDTO =new GenresLikeDTO();
            genresLikeDTO.setGenre(doc.getString("_id"));
            genresLikeDTO.setCount(doc.getInteger("count"));
            resultSet.add(genresLikeDTO);
        }
        return resultSet;
    }

    public boolean followTopCritic(BaseUser user, BaseUser topCritic) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean unfollowTopCritic(BaseUser user, BaseUser topCritic) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public ArrayList<TopCriticSuggestionDTO> getSuggestion(User usr, int page) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
    public boolean checkIfFollows(BaseUser user, BaseUser topCritic) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public int getNumberOfFollowers(TopCritic topCritic) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
