package it.unipi.dii.lsmsdb.rottenMovies.dbConnection;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.Document;

public class MongoDBConnector {
    private static final String connectionString = "mongodb://localhost:27017";
    private static final String databaseString = "rottenMovies";
    private static final String collectionStringMovie = "movie";
    private static final String collectionStringUser = "user";

    public MongoDBConnector() {
    }

    public void testConnection(){
        ConnectionString uri = new ConnectionString(connectionString);
        MongoClient myClient = MongoClients.create(uri);
        MongoDatabase db = myClient.getDatabase(databaseString);
        MongoCollection<Document> collection = db.getCollection(collectionStringMovie);
        try(MongoCursor<Document> cursor = collection.find().limit(1).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }

        myClient.close();
    }
}
