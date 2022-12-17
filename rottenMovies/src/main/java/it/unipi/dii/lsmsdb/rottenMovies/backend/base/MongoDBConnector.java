package it.unipi.dii.lsmsdb.rottenMovies.backend.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.print.Doc;

public abstract class MongoDBConnector {
    private static final String connectionString = "mongodb://localhost:27017";
    private static final String databaseString = "rottenMovies";


    public MongoClient getClient(){
        ConnectionString uri = new ConnectionString(connectionString);
        MongoClient myClient = MongoClients.create(uri);
        return myClient;
    }

    public MongoCollection<Document> returnCollection(MongoClient myClient, String connectionString){
        MongoDatabase db = myClient.getDatabase(databaseString);
        MongoCollection<Document> collection = db.getCollection(connectionString);
        return collection;
    }

    public void closeConnection(MongoClient myClient){
        myClient.close();
    }

    public void returnMovieByTitle(String title){
        MongoClient myClient = getClient();
        MongoCollection<Document> collection = returnCollection(myClient, "movie");

        //Usare questo codice se si ritorna con certezza un singolo elemento
         Document doc =  collection.find(Filters.eq("primaryTitle", title)).first();
        if (doc == null) {
            System.out.println("No results found.");
        } else {
            //the boilerplate code is going crazy
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(doc.toJson());
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
        }
        //Usare questo codice se non si è certi di tornare un singolo oggetto
        /*try(MongoCursor<Document> cursor = collection.find(Filters.eq("primaryTitle", title)).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
        */
        closeConnection(myClient);
    }
}

