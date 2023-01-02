package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.HallOfFameDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewMovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import it.unipi.dii.lsmsdb.rottenMovies.utils.*;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.*;


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

    public ArrayList<MovieDTO> executeSearchQuery(int page, SortOptions sort_opt, ReviewProjectionOptions proj_opt){
        MongoCollection<Document>  collection = getMovieCollection();
        Movie movie;
        String json_movie;
        ObjectMapper mapper = new ObjectMapper();
        try {
            BsonDocument bsonDocument = query.toBsonDocument(BsonDocument.class, Bson.DEFAULT_CODEC_REGISTRY);
            System.out.println("EXECUTING FIND: " + bsonDocument);
        } catch (Exception e){
            System.err.println(e);
        }
        FindIterable<Document> found = collection.find(query);
        Bson bson_check = sort_opt.getSort();
        if (bson_check != null){
            found = found.sort(sort_opt.getSort());
        }
        bson_check = proj_opt.getProjection();
        if (bson_check != null) {
            found.projection(proj_opt.getProjection());
        }

        if (page >= 0) { // only internally. Never return all movies without pagination on front-end
            query=null;
            found = found.skip(page * Constants.MOVIES_PER_PAGE).limit(Constants.MOVIES_PER_PAGE);
        }
        MongoCursor<Document> cursor = found.iterator();
        ArrayList<MovieDTO> movie_list = new ArrayList<>();
        System.out.println("========================RETURNED MOVIES============================");
        while(cursor.hasNext()){
            json_movie = cursor.next().toJson();
            try {
                movie = mapper.readValue(json_movie, Movie.class);
                System.out.println(movie);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            movie_list.add(new MovieDTO(movie));
        }
        return movie_list;
    }

    public boolean executeDeleteQuery(){
        ArrayList<MovieDTO> movies_to_delete = executeSearchQuery(-1,
                new SortOptions(SortOptionsEnum.NO_SORT, -1),
                new ReviewProjectionOptions(ReviewProjectionOptionsEnum.ALL, -1));
        MongoCollection<Document>  collectionMovie = getMovieCollection();
        MongoCollection<Document>  collectionUser = getUserCollection();
        boolean returnvalue=true;
        Bson filterUsr;
        Bson deleteReview, deletelast3;
        UpdateResult result3reviews,reviewsResult;
        Document moviefilter;
        for(MovieDTO movie:movies_to_delete) {
            ArrayList<ReviewMovieDTO> reviews = movie.getReviews(); // getting all the reviews
            moviefilter=new Document("primaryTitle",movie.getPrimaryTitle());
            for (ReviewMovieDTO review : reviews) {
                deleteReview = Updates.pull("reviews", moviefilter);
                deletelast3 = Updates.pull("last_3_reviews", moviefilter);
                filterUsr=eq("username", review.getCriticName());
                try{
                    reviewsResult=collectionUser.updateOne(filterUsr, deleteReview);
                    System.out.println("Modified document count: " + reviewsResult.getModifiedCount());
                    result3reviews=collectionUser.updateOne(filterUsr,deletelast3);
                    System.out.println("Modified document count: " + result3reviews.getModifiedCount());
                    if (result3reviews.getModifiedCount() == 1) {
                        //TODO: also remember to update the last_3_reviews field after deleting a movie
                        System.out.println("Last3review modified");
                    }
                }
                catch (MongoException me){
                    returnvalue=false;
                }
            }
        }

        try { // now I delete the movie from collection movie
            DeleteResult result = collectionMovie.deleteMany(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
    }

    public void queryBuildSearchByTitleExact(String title){
        System.out.println("SEARCH BY TITLE EXACT: " + title);
        Bson new_query = Filters.eq("primaryTitle", title);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchByTitle(String title){
        System.out.println("SEARCH BY TITLE: " + title);
        Bson new_query = Filters.regex("primaryTitle", title, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchById(ObjectId id){
        System.out.println("SEARCH BY ID: " + id);
        Bson new_query = Filters.eq("_id", id);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchPersonnel(String[] workers, boolean includeAll){
        System.out.println("SEARCH BY personnel: " + String.join(", ", workers));
        if (workers.length==0){
            return;
        }
        Bson new_query = null;
        for (String worker: workers) {
            worker = worker.trim();
            if (new_query == null){
                new_query = Filters.elemMatch("personnel", regex("primaryName", worker, "i"));
            } else {
                if (includeAll) {
                    new_query = Filters.and(new_query, Filters.elemMatch("personnel", regex("primaryName", worker, "i")));
                } else {
                    new_query = Filters.or(new_query, Filters.elemMatch("personnel", regex("primaryName", worker, "i")));
                }
            }
        }
        if (query == null){
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchGenres(String[] genres, boolean includeAll){
        System.out.println("SEARCH BY GENRES: " + String.join(", ", genres));
        if (genres.length==0){
            return;
        }
        Bson new_query = null;
        for (String genre: genres) {
            genre = genre.trim();
            if (new_query == null){
                new_query = Filters.regex("genres", genre, "i");
            } else {
                if (includeAll) {
                    new_query = Filters.and(new_query, Filters.regex("genres", genre, "i"));
                } else {
                    new_query = Filters.or(new_query, Filters.regex("genres", genre, "i"));
                }
            }
        }
        if (query == null){
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchByYear(int year, boolean afterYear){
        System.out.println("SEARCH BY YEAR: " + year + ((afterYear)?" AFTER":" BEFORE"));
        Bson new_query = null;
        if (afterYear) {
            new_query = Filters.and(gte("year", year));
        } else {
            new_query = Filters.and(lte("year", year));
        }
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    public void queryBuildSearchByTopRatings(int rating, boolean type){
        System.out.println("SEARCH BY TOP RATINGS: " + rating + ((type)?" GREATER":" LOWER"));
        Bson new_query;
        if(type)
            new_query =  Filters.gte("top_critic_rating", rating);
        else{
            new_query =  Filters.lte("top_critic_rating", rating);
        }
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }
    public void queryBuildsearchByUserRatings(int rating, boolean type){
        System.out.println("SEARCH BY USER RATING: " + rating + ((type)?" GREATER":" LOWER"));
        Bson new_query;
        if(type)
            new_query =  Filters.gte("user_rating", rating);
        else{
            new_query =  Filters.lte("user_rating", rating);
        }
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    private ArrayList<BasicDBObject> buildPersonnelField (Movie movie){
        ArrayList<BasicDBObject> personnelDBList= new ArrayList<>();
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
    public boolean update(Movie updated){
        MongoCollection<Document>  collection = getMovieCollection();
        ArrayList<BasicDBObject> personnelDBList = buildPersonnelField(updated);
        boolean returnvalue=true;
        Bson updates = Updates.combine(
                Updates.set("year", updated.getYear()),
                Updates.set("runtimeMinutes", updated.getRuntimeMinutes()),
                Updates.set("production_company", updated.getProductionCompany()),
                Updates.set("top_critic_status", updated.getTop_critic_status()),
                Updates.set("top_critic_rating", updated.getTop_critic_rating()),
                Updates.set("user_status", updated.getUser_status()),
                Updates.set("user_rating", updated.getUser_rating()),
                Updates.set("user_fresh_count", updated.getUser_fresh_count()),
                Updates.set("user_rotten_count", updated.getUser_rotten_count()),
                Updates.set("top_critic_fresh_count", updated.getTop_critic_fresh_count()),
                Updates.set("top_critic_rotten_count", updated.getTop_critic_rotten_count()),
                Updates.set("personnel",personnelDBList));
        /*
        if(updated.getCriticConsensus()!=null){ // not all movies have critic_consensus
            updates=Updates.combine(updates,Updates.set("critics_consensus", updated.getCriticConsensus()));
        }

         */

        try {
            query = null;
            queryBuildSearchById(updated.getId());
            UpdateResult result = collection.updateOne(query, updates);
            System.out.println("Modified document count: " + result.getModifiedCount());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            returnvalue=false;
        }
        query=null;
        return returnvalue;
    }
    public boolean insert(Movie newOne){
        MongoCollection<Document>  collection = getMovieCollection();
        ArrayList<BasicDBObject> personnelDBList = buildPersonnelField(newOne);
        boolean returnvalue=true;
        try {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("primaryTitle", newOne.getPrimaryTitle())
                    .append("year", newOne.getYear())
                    .append("runtimeMinutes", newOne.getRuntimeMinutes())
                    .append("production_company", newOne.getProductionCompany())
                    //.append("critics_consensus", "")
                    .append("top_critic_status", "")
                    .append("top_critic_rating", 0)
                    .append("user_status", "")
                    .append("user_rating", 0)
                    .append("user_fresh_count", 0)
                    .append("user_rotten_count", 0)
                    .append("top_critic_fresh_count", 0)
                    .append("top_critic_rotten_count", 0)
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

    public boolean delete(Movie movie) throws DAOException{
        queryBuildSearchById(movie.getId());
        boolean result = true;
        try{
            executeDeleteQuery();
        } catch (Exception e){
            System.err.println(e.getStackTrace());
            result = false;
        }
        query = null;
        return result;
    }

    public ArrayList<HallOfFameDTO> mostSuccesfullProductionHouses(int numberOfMovies){
        MongoCollection<Document>  collection = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.group("$production_company",
                                avg("top_critic_rating", "$top_critic_rating"),
                                avg("user_rating", "$user_rating"),
                                sum("count",1)),
                        Aggregates.match(gte("count",numberOfMovies)),
                        Aggregates.sort(Sorts.descending("top_critic_rating","user_rating")),
                        Aggregates.limit(Constants.MOVIES_PER_PAGE)
                )
        );
        ArrayList<HallOfFameDTO> resultSet = new ArrayList<>();
        HallOfFameDTO hallOfFameDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            hallOfFameDTO =new HallOfFameDTO();
            hallOfFameDTO.setSubject(doc.getString("_id"));
            hallOfFameDTO.setTop_critic_rating(doc.getDouble("top_critic_rating"));
            hallOfFameDTO.setUser_rating(doc.getDouble("user_rating"));
            hallOfFameDTO.setMovie_count(doc.getInteger("count"));
            resultSet.add(hallOfFameDTO);
        }
        return resultSet;
    }
    public ArrayList<HallOfFameDTO> mostSuccesfullGenres(int numberOfMovies){
        MongoCollection<Document>  collection = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$genres"),
                        Aggregates.group("$genres",
                                avg("top_critic_rating", "$top_critic_rating"),
                                avg("user_rating", "$user_rating"),
                                sum("count",1)),
                        Aggregates.match(gte("count",numberOfMovies)),
                        Aggregates.sort(Sorts.descending("top_critic_rating","user_rating")),
                        Aggregates.limit(Constants.MAXIMUM_NUMBER_OF_GENRES)
                )
        );
        ArrayList<HallOfFameDTO> resultSet = new ArrayList<>();
        HallOfFameDTO hallOfFameDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            hallOfFameDTO =new HallOfFameDTO();
            hallOfFameDTO.setSubject(doc.getString("_id"));
            hallOfFameDTO.setTop_critic_rating(doc.getDouble("top_critic_rating"));
            hallOfFameDTO.setUser_rating(doc.getDouble("user_rating"));
            hallOfFameDTO.setMovie_count(doc.getInteger("count"));
            resultSet.add(hallOfFameDTO);
        }
        return resultSet;
    }
    public Boolean insertNeo4j(String id, String title) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public Boolean deleteNeo4j(String id) throws DAOException, NoSuchRecordException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public Boolean updateNeo4j(String id, String newTitle) throws DAOException, NoSuchRecordException {
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

}
