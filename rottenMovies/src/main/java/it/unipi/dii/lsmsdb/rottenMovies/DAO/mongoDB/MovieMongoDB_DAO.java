package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;




/**
 * @author Fabio
 * @author Giacomo
 * @author Guillaume
 *
 * <class>MovieMongoDB_DAO</class> is the DAO class that queries the DB
 * based on different parameters like, title, year, topUserRating, topCriticsRatings
 */
public class MovieMongoDB_DAO extends BaseMongoDAO implements MovieDAO {

    private static final String collectionStringMovie = "movie";
    private static final String collectionStringUser = "user";
    /**
     * <method>searchByTitle</method> queries the DB for a specific movie base on the title
     * @param title is the title of the movie to search
     * @return a movie object
     */
    public Movie searchByTitle(String title){
        MongoClient myClient = getClient();
        Movie movie = searchByTitlePrivate(title, myClient);
        return movie;
    }

    private Movie searchByTitlePrivate(String title, MongoClient myClient) {
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
        return movie;

    }

    public List<Movie> searchByYearRange(int startYear, int endYear){
        MongoClient myClient = getClient();
        List<Movie> movie_list = searchByYearRangePrivate(startYear,endYear,myClient);
        closeConnection(myClient);
        return movie_list;
    }

    private List<Movie> searchByYearRangePrivate(int startYear, int endYear, MongoClient myClient) {
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
        List<Movie> movie_list = searchByTopRatingsPrivate(rating,type, myClient);
        closeConnection(myClient);
        return movie_list;
    }

    private List<Movie> searchByTopRatingsPrivate(int rating, boolean type, MongoClient myClient) {
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
        return  movie_list;
    }

    public List<Movie> searchByUserRatings(int rating, boolean type){
        MongoClient myClient = getClient();
        List<Movie>movie_list = searchByUserRatingsPrivate(rating, type, myClient);
        closeConnection(myClient);
        return movie_list;
    }

    private List<Movie> searchByUserRatingsPrivate(int rating, boolean type, MongoClient myClient) {
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

    public void deleteMovie(String title){
        MongoClient myClient = getClient();
        deleteMoviePrivate(title, myClient);
        closeConnection(myClient);
    }

    private void deleteMoviePrivate(String title, MongoClient myClient) {
        MongoCollection<Document>  collectionMovie = returnCollection(myClient, collectionStringMovie);
        MongoCollection<Document>  collectionUser = returnCollection(myClient, collectionStringUser);
        Movie movie = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collectionMovie.find(Filters.eq("primaryTitle", title)).first();
        String json_movie=null;
        if (doc == null) {
            System.out.println("No results found.");
        } else {
            json_movie = doc.toJson();
        }
        try {
            movie = mapper.readValue(json_movie, Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Bson queryMovie = eq("primaryTitle", title);

        try { // now I delete the movie from collection movie
            DeleteResult result = collectionMovie.deleteOne(queryMovie);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }
        ArrayList<Review> reviews = movie.getReviews(); // getting all the reviews
        Bson filter,deleteReview,deletelast3; // now i delete the movie for the user collection, both in last_3 and reviews
        UpdateResult result3reviews;
        for (Review r:
                reviews) {
            String username=r.getCriticName();
            System.out.println(username);
            filter=eq("username", username);
            deleteReview = Updates.pull("reviews", new Document("primaryTitle", title));
            deletelast3 = Updates.pull("last_3_reviews", new Document("primaryTitle", title));
            collectionUser.updateOne(filter, deleteReview);
            result3reviews=collectionUser.updateOne(filter,deletelast3);
            if(result3reviews.getModifiedCount()==1){
                // also remember to update the last_3_reviews field after deleting a movie
                System.out.println("Last3review modified");
            }
        }
    }

    public Boolean updateMovie(Movie updated){return true;} // needs implementation

    public Boolean insertMovie(Movie newOne){return true;} // needs implementation

}
