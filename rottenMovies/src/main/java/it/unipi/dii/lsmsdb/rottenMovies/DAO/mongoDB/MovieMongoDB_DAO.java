package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Bson query;

    public Bson getQuery() {
        return query;
    }

    public MovieDTO searchByTitle(String title){
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = new Movie();
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(queryBuildSearchByTitle(title).getQuery()).first();
        String json_movie;
        if (doc == null) {
            System.out.println("No results found.");
            return new MovieDTO(movie);
        } else {
            json_movie = doc.toJson();
        }
        try {
            movie = mapper.readValue(json_movie, Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new MovieDTO(movie);
    }
    /**
     * <method>searchByTitle</method> queries the DB for a specific movie base on the title
     * @param title is the title of the movie to search
     * @return a movie object
     */
    public MovieMongoDB_DAO queryBuildSearchByTitle(String title){
        Bson new_query = Filters.eq("primaryTitle", title);
        if (query == null) {
            query = new_query;
        }
        query = Filters.and(query, new_query);
        return this;
    }

    public MovieDTO searchById(ObjectId id){
        return new MovieDTO(_searchById(id));
    }
    private Movie _searchById(ObjectId id){
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        ObjectMapper mapper = new ObjectMapper();
        Document doc =  collection.find(Filters.eq("_id", id)).first();
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
    public List<MovieDTO> searchByYearRange(int startYear, int endYear){
        List<Movie> movies=_searchByYearRange(startYear, endYear);
        ArrayList<MovieDTO> movieDTOS = new ArrayList<MovieDTO>();
        for(Movie m: movies){
            movieDTOS.add(new MovieDTO(m));
        }
        return movieDTOS;
    }
    private List<Movie> _searchByYearRange(int startYear, int endYear){
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        Movie movie = null;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        MongoCursor<Document> cursor =  collection.find(and(
                gte("year", startYear), lte("year", endYear)
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
    public List<MovieDTO> searchByTopRatings(int rating, boolean type){
        List<Movie> movies=_searchByTopRatings(rating,type);
        ArrayList<MovieDTO> movieDTOS = new ArrayList<MovieDTO>();
        for(Movie m: movies){
            movieDTOS.add(new MovieDTO(m));
        }
        return movieDTOS;
    }
    private List<Movie> _searchByTopRatings(int rating, boolean type){
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
    public List<MovieDTO> searchByUserRatings(int rating, boolean type){
        List<Movie> movies=_searchByUserRatings(rating,type);
        ArrayList<MovieDTO> movieDTOS = new ArrayList<MovieDTO>();
        for(Movie m: movies){
            movieDTOS.add(new MovieDTO(m));
        }
        return movieDTOS;
    }
    private List<Movie> _searchByUserRatings(int rating, boolean type){
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
    public
 */
    public Boolean delete(MovieDTO toDelete){

        return delete(new Movie(toDelete));
    }
    private Boolean delete(Movie toDelete){
        MongoCollection<Document>  collectionMovie = returnCollection(myClient, collectionStringMovie);
        Bson queryMovie = eq("_id", toDelete.getId());

        try { // now I delete the movie from collection movie
            DeleteResult result = collectionMovie.deleteOne(queryMovie);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }

        // TODO: delete the review of the deleted movie
        return true;
        /*
        ArrayList<Review> reviews = movie.getReviews(); // getting all the reviews
        Bson filter,deleteReview,deletelast3; // now I delete the movie for the user collection, both in last_3 and reviews
        UpdateResult result3reviews;
        for (Review r: reviews) {
            String username=r.getCriticName();
            System.out.println(username);
            filter=eq("username", username);
            deleteReview = Updates.pull("reviews", new Document("primaryTitle", title));
            deletelast3 = Updates.pull("last_3_reviews", new Document("primaryTitle", title));
            collectionUser.updateOne(filter, deleteReview);
            result3reviews=collectionUser.updateOne(filter,deletelast3);
            if(result3reviews.getModifiedCount()==1){
                //TODO: also remember to update the last_3_reviews field after deleting a movie
                System.out.println("Last3review modified");
            }
        }

         */
    }
    private List<BasicDBObject> buildPersonnelField (Movie movie){
        List<BasicDBObject> personnelDBList=new ArrayList<BasicDBObject>();
        ArrayList<Personnel> personnelList=movie.getpersonnel();
        BasicDBObject worker;
        for (Personnel p: personnelList) {
            worker = new BasicDBObject();
            worker.put("primaryName",p.getPrimaryName());
            worker.put("category",p.getCategory());
            if (p.getJob()==null){
                worker.put("characters",p.getCharacters());
            }
            else {
                worker.put("job",p.getJob());
            }
            personnelDBList.add(worker);
        }
        return personnelDBList;
    }
    public Boolean update (MovieDTO updated){
        return update(new Movie(updated));
    }
    private Boolean update(Movie updated){
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        List<BasicDBObject> personnelDBList = buildPersonnelField(updated);

        Document movieFromDB = new Document().append("_id",  updated.getId());
        Bson updates = Updates.combine(
                Updates.set("year", updated.getYear()),
                Updates.set("runtimeMinutes", updated.getRuntimeMinutes()),
                Updates.set("production_company", updated.getProductionCompany()),
                Updates.set("tomatometer_status", updated.getTomatometerStatus()),
                Updates.set("tomatometer_rating", updated.gettomatometerRating()),
                Updates.set("audience_status", updated.getAudienceStatus()),
                Updates.set("audience_rating", updated.getaudienceRating()),
                Updates.set("audience_count", updated.getAudienceCount()),
                Updates.set("tomatometer_fresh_critics_count", updated.getTomatometerFreshCriticsCount()),
                Updates.set("tomatometer_rotten_critics_count", updated.getTomatometerRottenCriticsCount()),
                Updates.set("personnel",personnelDBList));
        if(updated.getCriticConsensus()!=null){ // not all movies have critic_consensus
            updates=Updates.combine(updates,Updates.set("critics_consensus", updated.getCriticConsensus()));
        }
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            UpdateResult result = collection.updateOne(movieFromDB, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
        return true;
    }
    public Boolean insert (MovieDTO newOne){
        return insert(new Movie(newOne));
    }
    private Boolean insert(Movie newOne){
        MongoCollection<Document>  collection = returnCollection(myClient, collectionStringMovie);
        List<BasicDBObject> personnelDBList = buildPersonnelField(newOne);

        try {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("primaryTitle", newOne.getPrimaryTitle())
                    .append("year", newOne.getYear())
                    .append("runtimeMinutes", newOne.getRuntimeMinutes())
                    .append("production_company", newOne.getProductionCompany())
                    .append("critics_consensus", "")
                    .append("tomatometer_status", "")
                    .append("tomatometer_rating", 0)
                    .append("audience_status", "")
                    .append("audience_rating", 0)
                    .append("audience_count", 0)
                    .append("tomatometer_fresh_critics_count", 0)
                    .append("tomatometer_rotten_critics_count", 0)
                    .append("personnel", personnelDBList)
                    .append("review", new ArrayList<BasicDBObject>()));

            System.out.println("Success! Inserted document id: " + result.getInsertedId());
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
            return false;
        }
        return true;
    }

    public Boolean insertNeo4j(String id, String title) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

}
