package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;



public class BaseUserMongoDB_DAO extends BaseMongoDAO implements BaseUserDAO {
    private static final String collectionStringMovie = "movie";
    private static final String collectionStringUser = "user";


    public BaseUser getUserByUserName(String name) {
        MongoClient myClient = getClient();
        MongoCollection<Document> collection = returnCollection(myClient, collectionStringUser);
        BaseUser baseUser = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(Filters.eq("username", name)).first();
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

    public List<BaseUser> getUser() {
        MongoClient myClient = getClient();
        MongoCollection<Document> collection = returnCollection(myClient, collectionStringUser);
        List<BaseUser> baseUserList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find().iterator();
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

}
