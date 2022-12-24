package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fabio
 * @author Guillaume
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
    public MongoCollection<Document> getCollection(){
        return returnCollection(myClient, consts.COLLECTION_STRING_USER);
    }
    public ArrayList<BaseUserDTO> executeSearchQuery(int page){
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);
        ObjectMapper mapper = new ObjectMapper();
        FindIterable found = collection.find(query);
        if (page >= 0) { // only internally. Never return all movies without pagination on front-end
            query=null;
            found = found.skip(page * consts.USERS_PER_PAGE).limit(consts.USERS_PER_PAGE);
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
        Bson new_query = Filters.eq("username", username);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    /**
     * <method>getUserByUsername</method> queries the DB based on a username to search
     * @param Username is the username to search
     * @return a BaseUser model
     */
    public BaseUser getByUsername(String Username) {
        MongoCollection<Document> collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);
        BaseUser simpleUser = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(Filters.eq("username", Username)).first();
        String json_user;
        if (doc == null) {
            System.out.println("No results found.");
            return null;
        } else {
            json_user = doc.toJson();
        }
        try {
            if (doc.containsKey("date_of_birth")) {
                simpleUser = mapper.readValue(json_user, User.class);
            } else {
                simpleUser = mapper.readValue(json_user, TopCritic.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return simpleUser;
    }
    public BaseUser getById(ObjectId id){
        MongoCollection<Document> collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);
        BaseUser simpleUser = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(Filters.eq("_id", id)).first();
        String json_user;
        if (doc == null) {
            System.out.println("No results found.");
            return null;
        } else {
            json_user = doc.toJson();
        }
        try {
            if (doc.containsKey("date_of_birth")) {
                simpleUser = mapper.readValue(json_user, User.class);
            } else {
                simpleUser = mapper.readValue(json_user, TopCritic.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return simpleUser;
    }
    /**
     * <method>getUser</method> queries the DB for all user
     * @return a list containing all BaseUser
     */
    public List<BaseUser> getAll() {
        MongoCollection<Document> collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);
        List<BaseUser> simpleUserList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find(exists("date_of_birth", true)).iterator();
        BaseUser simpleUser = null;
        String json_user;
        while(cursor.hasNext()){
            Document doc = cursor.next();
            json_user = doc.toJson();
            //System.out.println(json_movie);
            try {
                if (doc.containsKey("date_of_birth")) {
                    simpleUser = mapper.readValue(json_user, User.class);
                } else {
                    simpleUser = mapper.readValue(json_user, TopCritic.class);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            simpleUserList.add(simpleUser);
        }
        return simpleUserList;
    }

    public Boolean insert(BaseUser usr){
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);
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
    public Boolean modify(BaseUser usr){
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_USER);

        Document baseUserFromDB = new Document().append("_id",  usr.getId());
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
            UpdateResult result = collection.updateOne(baseUserFromDB, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
        return true;
    }

    public Boolean delete(BaseUser user) {
        MongoCollection<Document>  collectionUser = returnCollection(myClient, consts.COLLECTION_STRING_USER);

        Bson queryUser = eq("_id", user.getId());
        user.setFirstName("[[IS_GOING_TO_BE_DELETED]]"); // TODO: refactor to global constant
        modify(user);
        ReviewDAO reviewdao = (ReviewDAO) new ReviewMongoDB_DAO(); // TODO: maybe use DAOLocator
        try {
            reviewdao.updateReviewsByDeletedBaseUser(user);
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        try { // now I delete the user from collection user
            DeleteResult result = collectionUser.deleteOne(queryUser);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            return false;
        }

        // TODO: delete the review
        return true;
        /*

        List<SimplyfiedReview> reviews = simpleUser.getReviews();

        for (SimplyfiedReview r: reviews) {
            Document doc2=collectionMovie.find(eq("primaryTitle", r.getPrimaryTitle())).projection(fields(include("primaryTitle","review"),excludeId(), slice("review", r.getIndex(),1))).first();

            System.out.println(doc2.get("review"));
        }
         */
    }
    public User getMostReviewUser() throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
    public TopCritic getMostFollowedCritic() throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
    public boolean createBaseUser(String name, boolean isTop) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean deleteBaseUser(String name, boolean isTop) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean followTopCritic(String userName, String topCriticName) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean unfollowTopCritic(String userName, String topCriticName) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
