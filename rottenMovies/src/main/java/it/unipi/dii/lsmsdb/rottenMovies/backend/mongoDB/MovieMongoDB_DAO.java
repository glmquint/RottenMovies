package it.unipi.dii.lsmsdb.rottenMovies.backend.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.dii.lsmsdb.rottenMovies.backend.base.MongoDBConnector;
import it.unipi.dii.lsmsdb.rottenMovies.backend.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.Document;

import java.util.Map;

public class MovieMongoDB_DAO extends MongoDBConnector implements MovieDAO {
    private static final String collectionStringMovie = "movie";
    public Movie searchByTitle(String title){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            //Map<String,Object> map = mapper.readValue(returnMovieByTitle(collection, title), Map.class);
            movie = mapper.readValue(returnMovieByTitle(collection, title), Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /*Gson gson = new Gson(); // Or use new GsonBuilder().create();
        movie = gson.fromJson(returnMovieByTitle(collection, title), Movie.class);
        */
        closeConnection(myClient);
        return movie;
    }
    public Movie searchByYear(int year){return null;}
    public Movie searchByTopRatings(int rating){return null;}
    public Movie searchByUserRatings(int rating){return null;}

    private String returnMovieByTitle(MongoCollection<Document> collection,String title){

        //Usare questo codice se si ritorna con certezza un singolo elemento
        Document doc =  collection.find(Filters.eq("primaryTitle", title)).first();
        if (doc == null) {
            System.out.println("No results found.");
            return null;
        } else {
            return doc.toJson();
            //the boilerplate code is going crazy
            /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(doc.toJson());
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);*/
        }
        //Usare questo codice se non si è certi di tornare un singolo oggetto
        /*try(MongoCursor<Document> cursor = collection.find(Filters.eq("primaryTitle", title)).iterator()) {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
        */
    }
}
