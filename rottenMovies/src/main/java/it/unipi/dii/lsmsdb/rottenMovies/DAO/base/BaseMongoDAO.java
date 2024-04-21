package it.unipi.dii.lsmsdb.rottenMovies.DAO.base;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * <class>MongoDBConnector</class>
 *  is the base connector for the MongoDB,
 * it offers methods to create a connection to the server (getClient),
 * get a collection from the DB (getCollection) and close the connection
 * to the database (closeConnection)
 */
public abstract class BaseMongoDAO implements AutoCloseable{
    protected final MongoClient myClient;
    protected Bson query;

    public Bson getQuery() {
        return query;
    }

    public BaseMongoDAO(){
        System.out.println("[DEBUG]:connection established");
        ConnectionString uri = new ConnectionString(Constants.MONGO_CONNECTION_STRING);
        MongoClientSettings mcs = MongoClientSettings.builder()
                .applyConnectionString(uri)
                .readPreference(ReadPreference.nearest())
                .retryWrites(true)
                .writeConcern(WriteConcern.W2).build();
        this.myClient = MongoClients.create(mcs);
        query = null;
    }

    private MongoCollection<Document> returnCollection(String collectionString){
        MongoDatabase db = myClient.getDatabase(Constants.MONGO_DATABASE_STRING);
        MongoCollection<Document> collection = db.getCollection(collectionString);
        return collection;
    }
    public MongoCollection<Document> getMovieCollection(){
       return returnCollection(Constants.COLLECTION_STRING_MOVIE);
    }
    public MongoCollection<Document> getUserCollection(){
        return returnCollection(Constants.COLLECTION_STRING_USER);
    }

    /**
     * <method>closeConnection</method> closes the connection to the DB
     */
    @Override
    public void close () throws RuntimeException{
        System.out.println("[DEBUG]:closed connection");
        this.myClient.close();
    }


}

