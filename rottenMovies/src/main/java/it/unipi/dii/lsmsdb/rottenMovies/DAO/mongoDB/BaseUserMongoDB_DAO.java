package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
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
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * @author Fabio
 * @author Guillaume
 * @author Giacomo
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
    public ArrayList<BaseUserDTO> executeSearchQuery(int page){
        MongoCollection<Document>  collection = getUserCollection();
        ObjectMapper mapper = new ObjectMapper();
        FindIterable<Document> found = collection.find(query);
        if (page >= 0) { // only internally. Never return all users without pagination on front-end
            query=null;
            found = found.skip(page * Constants.USERS_PER_PAGE).limit(Constants.USERS_PER_PAGE);
        }
        MongoCursor<Document> cursor = found.iterator();
        ArrayList<BaseUserDTO> user_list = new ArrayList<>();
        BaseUser simpleUser;
        String json_user;
        Document doc;
        while(cursor.hasNext()){
            doc = cursor.next();
            json_user = doc.toJson();
            try {
                if (doc.containsKey("date_of_birth")) {
                    simpleUser = mapper.readValue(json_user, User.class);
                    user_list.add(new UserDTO((User) simpleUser));
                } else {
                    simpleUser = mapper.readValue(json_user, TopCritic.class);
                    user_list.add(new TopCriticDTO((TopCritic) simpleUser));
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return user_list;
    }
    public void queryBuildSearchByUsername(String username){
        Bson new_query = Filters.regex("username", username, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    public void queryBuildSearchByFirstName(String firstname){
        Bson new_query = Filters.regex("first_name", firstname, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    public void queryBuildSearchByLastName(String lastname){
        Bson new_query = Filters.regex("last_name", lastname, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

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

    public void queryBuildSearchById(ObjectId id){
        Bson new_query = Filters.eq("_id", id);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public boolean insert(BaseUser usr){
        MongoCollection<Document>  collection = getUserCollection();
        try {
            Document newdoc = new Document()
                    .append("_id", new ObjectId())
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
            return false;
        }
        // also remember to add the user in Neo4j
        return true;
    }
    public boolean update(BaseUser usr) throws DAOException{
        MongoCollection<Document>  collection = getUserCollection();
        boolean returnvalue=true;
        Bson updates = Updates.combine(
                    Updates.set("password", usr.getPassword()),
                    Updates.set("first_name", usr.getFirstName()),
                    Updates.set("last_name", usr.getLastName()),
                    Updates.set("registration_date", usr.getRegistrationDate()));
        if (usr instanceof User){
            updates = Updates.combine(updates, Updates.set("date_of_birth", ((User)usr).getBirthdayDate()));
        }

        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            query=null;
            queryBuildSearchById(usr.getId());
            UpdateResult result = collection.updateOne(query, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
    }

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

    public boolean executeDeleteQuery() {
        ArrayList<BaseUserDTO> users_to_delete = executeSearchQuery(-1);
        MongoCollection<Document>  collectionUser = getUserCollection();
        MongoCollection<Document>  collectionMovie = getMovieCollection();
        boolean returnvalue=true;
        ArrayList<SimplyfiedReviewDTO> listrev;
        Document set;
        for(BaseUserDTO b:users_to_delete){ // now I set the critic_name in the review field of movie as DELETED_USER
            listrev=b.getReviews();
            for(SimplyfiedReviewDTO s:listrev){
                set=new Document("$set", new Document("review."+s.getIndex()+".critic_name",Constants.DELETED_USER));
                collectionMovie.updateOne(new Document("_id",s.getMovieID()), set);
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
    public BaseUserDTO getMostReviewUser() throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
    public TopCriticDTO getMostFollowedCritic() throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean followTopCritic(String userName, String topCriticName) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean unfollowTopCritic(String userName, String topCriticName) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
