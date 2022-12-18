package it.unipi.dii.lsmsdb.rottenMovies.backend.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.dii.lsmsdb.rottenMovies.backend.base.MongoDBConnector;
import it.unipi.dii.lsmsdb.rottenMovies.backend.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.Document;


/**
 * @author Fabio
 * <class>MovieMongoDB_DAO</class> is the DAO class that queries the DB
 * based on different parameters like, title, year, topUserRating, topCriticsRatings
 */
public class MovieMongoDB_DAO extends MongoDBConnector implements MovieDAO {

    private static final String collectionStringMovie = "movie";

    /**
     * <method>searchByTitle</method> queries the DB for a specific movie base on the title
     * @param title is the title of the movie to search
     * @return a movie object
     */
    public Movie searchByTitle(String title){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
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
