package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
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
    private static final String collectionStringMovie = "movie";
    private static final String collectionStringUser = "user";

    /**
     * <method>getUserByUsername</method> queries the DB based on a username to search
     * @param Username is the username to search
     * @return a BaseUser model
     */
    public BaseUser getUserByUserName(String Username) {
        MongoClient myClient = getClient();
        MongoCollection<Document> collection = returnCollection(myClient, collectionStringUser);
        BaseUser baseUser = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(Filters.eq("username", Username)).first();
        String json_movie;
        if (doc == null) {
            System.out.println("No results found.");
            return null;
        } else {
            json_movie = doc.toJson();
        }
        try {
            baseUser = mapper.readValue(json_movie, BaseUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        closeConnection(myClient);
        return baseUser;
    }

    /**
     * <method>getUser</method> queries the DB for all user
     * @return a list containing all BaseUser
     */
    public List<BaseUser> getUser() {
        MongoClient myClient = getClient();
        MongoCollection<Document> collection = returnCollection(myClient, collectionStringUser);
        List<BaseUser> baseUserList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find(exists("date_of_birth", true)).iterator();
        BaseUser baseUser = null;
        String json_user;
        while(cursor.hasNext()){
            json_user = cursor.next().toJson();
            //System.out.println(json_movie);
            try {
                baseUser = mapper.readValue(json_user, BaseUser.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            baseUserList.add(baseUser);
        }
        closeConnection(myClient);
        return baseUserList;
    }

    public Boolean insertBaseUser(BaseUser usr){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringUser);
        try {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("username", usr.getUsername())
                    .append("password", usr.getPassword())
                    .append("first_name", usr.getFirstName())
                    .append("last_name", usr.getLastName())
                    .append("registration_date", usr.getRegistrationDate())
                    .append("last_3_reviews", new ArrayList<BasicDBObject>())
                    .append("reviews", new ArrayList<BasicDBObject>())
                    .append("date_of_birth", usr.getBirthdayDate()));
            System.out.println("Success! Inserted document id: " + result.getInsertedId());
        }
        catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
            return false;
        }
        closeConnection(myClient);
        return true;
    }
    public Boolean modifyBaseUser(BaseUser usr){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringUser);

        Document baseUserFromDB = new Document().append("username",  usr.getUsername());
        Bson updates = Updates.combine(
                    Updates.set("password", usr.getPassword()),
                    Updates.set("first_name", usr.getFirstName()),
                    Updates.set("last_name", usr.getLastName()),
                    Updates.set("registration_date", usr.getRegistrationDate()),
                    Updates.set("date_of_birth", usr.getBirthdayDate()));

        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            UpdateResult result = collection.updateOne(baseUserFromDB, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }

        closeConnection(myClient);
        return true;
    }

}
