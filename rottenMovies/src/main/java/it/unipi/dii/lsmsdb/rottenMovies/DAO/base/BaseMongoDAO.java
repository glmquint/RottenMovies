package it.unipi.dii.lsmsdb.rottenMovies.DAO.base;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @author Fabio
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
        System.out.println("connection established");
        ConnectionString uri = new ConnectionString(Constants.CONNECTION_STRING);
        this.myClient = MongoClients.create(uri);
        query = null;
    }

    /**
     * <method>returnCollection</method>  returns a collection from the connected DB
     * @param myClient is the object used to handle the connection
     * @param connectionString is the string used to select a particular collection
     * @return a document to make operation on the collection
     */
    public MongoCollection<Document> returnCollection(MongoClient myClient, String connectionString){
        MongoDatabase db = myClient.getDatabase(Constants.DATABASE_STRING);
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

