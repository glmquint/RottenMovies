package it.unipi.dii.lsmsdb.rottenMovies.backend.base;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.Document;

/**
 * @author Fabio
 * <class>MongoDBConnector</class>
 *  is the base connector for the MongoDB,
 * it offers methods to create a connection to the server (getClient),
 * get a collection from the DB (getCollection) and close the connection
 * to the database (closeConnection)
 */
public abstract class MongoDBConnector {
    private static final String connectionString = "mongodb://localhost:27017";
    private static final String databaseString = "rottenMovies";

    /**
     * <method>getClient</method> create a connection to the mongoDB
     * @return a myClient object to the caller for handling the connection
     */
    public MongoClient getClient(){
        ConnectionString uri = new ConnectionString(connectionString);
        MongoClient myClient = MongoClients.create(uri);
        return myClient;
    }

    /**
     * <method>returnCollection</method>  returns a collection from the connected DB
     * @param myClient is the object used to handle the connection
     * @param connectionString is the string used to select a particular collection
     * @return a document to make operation on the collection
     */
    public MongoCollection<Document> returnCollection(MongoClient myClient, String connectionString){
        MongoDatabase db = myClient.getDatabase(databaseString);
        MongoCollection<Document> collection = db.getCollection(connectionString);
        return collection;
    }

    /**
     * <method>closeConnection</method> closes the connection to the DB
     * @param myClient is the connection to close
     */
    public void closeConnection(MongoClient myClient){
        myClient.close();
    }


}

