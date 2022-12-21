package it.unipi.dii.lsmsdb.rottenMovies.DAO.base;

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
public abstract class BaseMongoDAO implements AutoCloseable{
    private static final String connectionString = "mongodb://localhost:27017";
    private static final String databaseString = "rottenMovies";

    protected final MongoClient myClient;

    public BaseMongoDAO(){
        System.out.println("connection established");
        ConnectionString uri = new ConnectionString(connectionString);
        this.myClient = MongoClients.create(uri);
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
     */
    @Override
    public void close () throws RuntimeException{
        System.out.println("closed conenction");
        this.myClient.close();
    }


}

