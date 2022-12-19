package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;




/**
 * @author Fabio
 * <class>MovieMongoDB_DAO</class> is the DAO class that queries the DB
 * based on different parameters like, title, year, topUserRating, topCriticsRatings
 */
public class MovieMongoDB_DAO extends BaseMongoDAO implements MovieDAO {

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
        Document doc =  collection.find(Filters.eq("primaryTitle", title)).first();
        String json_movie;
        if (doc == null) {
            System.out.println("No results found.");
            return null;
        } else {
            json_movie = doc.toJson();
        }
        try {
            movie = mapper.readValue(json_movie, Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        closeConnection(myClient);
        return movie;
    }

    public List<Movie> searchByYearRange(int startYear, int endYear){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find(and(
                gte("year", endYear), lte("year", startYear)
            )).iterator();
        List<Movie> movie_list = new ArrayList<>();
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            //System.out.println(json_movie);
            try {
                movie = mapper.readValue(json_movie, Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(movie);
        }
        return movie_list;
    }
    public List<Movie> searchByTopRatings(int rating, boolean type){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor;
        if(type)
            cursor =  collection.find(
                    gte("tomatometer_rating", rating)
            ).sort(orderBy(descending("tomatometer_rating")))
                    .iterator();
        else{
            cursor =  collection.find(
                    lte("tomatometer_rating", rating)
            ).sort(orderBy(descending("tomatometer_rating")))
                    .iterator();
        }
        List<Movie> movie_list = new ArrayList<>();
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            //System.out.println(json_movie);
            try {
                movie = mapper.readValue(json_movie, Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(movie);
        }
        return movie_list;
    }
    public List<Movie> searchByUserRatings(int rating, boolean type){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor;
        if(type)
            cursor =  collection.find(
                    gte("audience_rating", rating)
                ).sort(orderBy(descending("audience_rating")))
                    .iterator();
        else{
            cursor =  collection.find(
                    lte("audience_rating", rating)
            ).sort(orderBy(descending("audience_rating")))
                    .iterator();
        }
        List<Movie> movie_list = new ArrayList<>();
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            //System.out.println(json_movie);
            try {
                movie = mapper.readValue(json_movie, Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(movie);
        }
        return movie_list;
    }
/*
    funzione per cercare movie in un solo anno, è un sottocaso di searchByYearRange dove
    startYear == endYear
   public List<Movie> searchByYear(int year){
        MongoClient myClient = getClient();
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find(Filters.eq("year", year)).iterator();
        List<Movie> movie_list = new ArrayList<>();
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            //System.out.println(json_movie);
            try {
                movie = mapper.readValue(json_movie, Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(movie);
        }
        return movie_list;
    }
 */

}
