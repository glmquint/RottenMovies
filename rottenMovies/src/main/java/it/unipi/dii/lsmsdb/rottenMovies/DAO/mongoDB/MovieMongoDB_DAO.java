package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

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

    public MovieMongoDB_DAO() {
        super();
    }

    public MongoCollection<Document> getCollection(){
        return returnCollection(myClient, consts.COLLECTION_STRING_MOVIE);
    }

    public ArrayList<MovieDTO> executeSearchQuery(int page){
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_MOVIE);
        Movie movie;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        FindIterable found = collection.find(query);
        if (page >= 0) { // only internally. Never return all movies without pagination on front-end
            query=null;
            found = found.skip(page * consts.MOVIES_PER_PAGE).limit(consts.MOVIES_PER_PAGE);
        }
        MongoCursor<Document> cursor = found.iterator();
        ArrayList<MovieDTO> movie_list = new ArrayList<>();
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            try {
                movie = mapper.readValue(json_movie, Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(new MovieDTO(movie));
        }
        return movie_list;
    }

    public Boolean executeDeleteQuery(){
        ArrayList<MovieDTO> movies_to_delete = executeSearchQuery(-1);
        // TODO: delete the user review of the deleted movie before executing deleteMany
        MongoCollection<Document>  collectionMovie = returnCollection(myClient, consts.COLLECTION_STRING_MOVIE);
        Boolean returnvalue=true;
        try { // now I delete the movie from collection movie
            DeleteResult result = collectionMovie.deleteMany(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
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

    public void queryBuildSearchByTitle(String title){
        Bson new_query = Filters.eq("primaryTitle", title);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchByTitleContains(String title){
        Bson new_query = Filters.regex("primaryTitle", title, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchById(ObjectId id){
        Bson new_query = Filters.eq("_id", id);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    public void queryBuildSearchByTopRatings(int rating, boolean type){
        Bson new_query;
        if(type)
            new_query =  Filters.gte("tomatometer_rating", rating);
        else{
            new_query =  Filters.lte("tomatometer_rating", rating);
        }
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    public void queryBuildsearchByUserRatings(int rating, boolean type){
        Bson new_query;
        if(type)
            new_query =  Filters.gte("audience_rating", rating);
        else{
            new_query =  Filters.lte("audience_rating", rating);
        }
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
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

    private ArrayList<BasicDBObject> buildPersonnelField (Movie movie){
        ArrayList<BasicDBObject> personnelDBList=new ArrayList<BasicDBObject>();
        ArrayList<Personnel> personnelList=movie.getpersonnel();
        BasicDBObject worker;
        if (personnelList != null){
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
        }
        return personnelDBList;
    }
    public Boolean update(MovieDTO update){
        Movie updated=new Movie(update);
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_MOVIE);
        List<BasicDBObject> personnelDBList = buildPersonnelField(updated);
        Boolean returnvalue=true;
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
            query = null;
            queryBuildSearchById(updated.getId());
            UpdateResult result = collection.updateOne(query, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
    }
    public Boolean insert(MovieDTO newOne){
        MongoCollection<Document>  collection = returnCollection(myClient, consts.COLLECTION_STRING_MOVIE);
        List<BasicDBObject> personnelDBList = buildPersonnelField(new Movie(newOne));
        Boolean returnvalue=true;
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
            returnvalue = false;
        }
        query=null;
        return returnvalue;
    }

}
