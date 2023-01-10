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
import it.unipi.dii.lsmsdb.rottenMovies.DTO.YearMonthReviewDTO;
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
    /**
     * <method>executeSearchQuery</method> executes a serch query in movie collection
     * @param page, sort_opt, proj_opt
     * @return an Arraylist of MovieDTO
     * @throws DAOException
     */

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
    /**
     * <method>executeDeleteQuery</method> executes a delete query in movie collection
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
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
    /**
     * <method>queryBuildSearchByTitleExact</method> builds a new query for finding a movie by its exact primaryTitle if none exists
     * or keeps concatenating the new query to the previous one
     * @param title
     * @throws DAOException
     */
    public void queryBuildSearchByTitleExact(String title){
        System.out.println("SEARCH BY TITLE EXACT: " + title);
        Bson new_query = Filters.eq("primaryTitle", title);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchByTitle</method> builds a new query for finding a movie by its primaryTitle if none exists
     * or keeps concatenating the new query to the previous one
     * @param title
     * @throws DAOException
     */
    public void queryBuildSearchByTitle(String title){
        System.out.println("SEARCH BY TITLE: " + title);
        Bson new_query = Filters.regex("primaryTitle", title, "i");
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchById</method> builds a new query for finding a movie by its id if none exists
     * or keeps concatenating the new query to the previous one
     * @param id
     * @throws DAOException
     */

    public void queryBuildSearchById(ObjectId id){
        System.out.println("SEARCH BY ID: " + id);
        Bson new_query = Filters.eq("_id", id);
        if (query == null) {
            query = new_query;
            return;
        }
        query = Filters.and(query, new_query);
    }

    /**
     * <method>queryBuildSearchPersonnel</method> builds a new query for finding a movie by its personnel if none exists
     * or keeps concatenating the new query to the previous one
     * @param workers, includeAll
     * @throws DAOException
     */
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

    /**
     * <method>queryBuildSearchGenres</method> builds a new query for finding a movie by its genres if none exists
     * or keeps concatenating the new query to the previous one
     * @param genres, includeAll
     * @throws DAOException
     */
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

    /**
     * <method>queryBuildSearchByYear</method> builds a new query for finding a movie by its year of release if none exists
     * or keeps concatenating the new query to the previous one
     * @param year, afterYear
     * @throws DAOException
     */

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

    /**
     * <method>queryBuildSearchByTopRatings</method> builds a new query for finding a movie based on top critic rating if none exists
     * or keeps concatenating the new query to the previous one
     * @param rating, type
     * @throws DAOException
     */
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

    /**
     * <method>queryBuildsearchByUserRatings</method> builds a new query for finding a movie based on user rating if none exists
     * or keeps concatenating the new query to the previous one
     * @param rating, type
     * @throws DAOException
     */
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
        } else {
            for (int i = 0; i < 10; i++) {
                worker = new BasicDBObject();
                worker.put("primaryName","");
                worker.put("category","");
                worker.put("job","");
                personnelDBList.add(worker);
            }
        }
        return personnelDBList;
    }

    /**
     * <method>update</method> updates the information of a movie inside the collection
     * @return true in case of success, false otherwise
     * @param updated is the movie model
     * @throws DAOException
     */
    public boolean update(Movie updated){
        MongoCollection<Document>  collection = getMovieCollection();
        ArrayList<BasicDBObject> personnelDBList = buildPersonnelField(updated);
        boolean returnvalue=true;
        Bson updates = Updates.combine(
                Updates.set("year", updated.getYear()),
                Updates.set("runtimeMinutes", updated.getRuntimeMinutes()),
                Updates.set("production_company", updated.getProductionCompany()),
                Updates.set("genres", updated.getGenres()),
                Updates.set("poster_url", updated.getPosterUrl()),
                Updates.set("personnel",personnelDBList));
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

    /**
     * <method>insert</method> insert a new movie inside the collection
     * @return the ObjectId of the inserted movie
     * @param newOne is the movie model
     * @throws DAOException
     */
    public ObjectId insert(Movie newOne){
        MongoCollection<Document>  collection = getMovieCollection();
        ArrayList<BasicDBObject> personnelDBList = buildPersonnelField(newOne);
        ObjectId returnvalue=new ObjectId();
        try {
            InsertOneResult result = collection.insertOne(new Document()
                    .append("_id", returnvalue)
                    .append("primaryTitle", newOne.getPrimaryTitle())
                    .append("year", newOne.getYear())
                    .append("runtimeMinutes", newOne.getRuntimeMinutes())
                    .append("production_company", newOne.getProductionCompany())
                    .append("genres", newOne.getGenres())
                    .append("top_critic_status", "")
                    .append("top_critic_rating", 0)
                    .append("user_status", "")
                    .append("user_rating", 0)
                    .append("user_fresh_count", 0)
                    .append("user_rotten_count", 0)
                    .append("top_critic_fresh_count", 0)
                    .append("top_critic_rotten_count", 0)
                    .append("poster_url",newOne.getPosterUrl())
                    .append("personnel", personnelDBList)
                    .append("review", new ArrayList<BasicDBObject>()));

            System.out.println("Success! Inserted document id: " + result.getInsertedId());
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
            returnvalue = null;
        }
        query=null;
        return returnvalue;
    }

    /**
     * <method>delete</method> delete a specific movie inside the collection
     * @return true in case of success, false otherwise
     * @param movie is the movie model
     * @throws DAOException
     */
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

    /**
     * <method>mostSuccesfullProductionHouses</method>  implement the aggregation for finding most succesfull production houses
     * @return an ArrayList of HallOfFameDTO
     * @param numberOfMovies is minimum number of movies produced by the production house, opt is the SortOptions
     * @throws DAOException
     */
    public ArrayList<HallOfFameDTO> mostSuccesfullProductionHouses(int numberOfMovies, SortOptions opt){
        MongoCollection<Document>  collection = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.group("$production_company",
                                avg("top_critic_rating", "$top_critic_rating"),
                                avg("user_rating", "$user_rating"),
                                sum("count",1)),
                        Aggregates.match(gte("count",numberOfMovies)),
                        Aggregates.sort(opt.getBsonAggregationSort()),
                        Aggregates.limit(Constants.HALL_OF_FAME_ELEMENT_NUMBERS)
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
    /**
     * <method>mostSuccesfullGenres</method> implement the aggregation for finding most successful genres
     * @return an ArrayList of HallOfFameDTO
     * @param numberOfMovies is minimum number of movies per genre, opt is the SortOptions
     * @throws DAOException
     */
    public ArrayList<HallOfFameDTO> mostSuccesfullGenres(int numberOfMovies, SortOptions opt){
        MongoCollection<Document>  collection = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$genres"),
                        Aggregates.group("$genres",
                                avg("top_critic_rating", "$top_critic_rating"),
                                avg("user_rating", "$user_rating"),
                                sum("count",1)),
                        Aggregates.match(gte("count",numberOfMovies)),
                        Aggregates.sort(opt.getBsonAggregationSort()),
                        Aggregates.limit(Constants.HALL_OF_FAME_ELEMENT_NUMBERS)
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

    /**
     * <method>bestYearsBasedOnRatings</method> implement the aggregation for finding most successful years
     * @return an ArrayList of HallOfFameDTO
     * @param numberOfMovies is minimum number of movies per year, opt is the SortOptions
     * @throws DAOException
     */
    public ArrayList<HallOfFameDTO> bestYearsBasedOnRatings (int numberOfMovies, SortOptions opt){
        MongoCollection<Document>  collection = getMovieCollection();
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.group("$year",
                                avg("top_critic_rating", "$top_critic_rating"),
                                avg("user_rating", "$user_rating"),
                                sum("count",1)),
                        Aggregates.match(gte("count",numberOfMovies)),
                        Aggregates.sort(opt.getBsonAggregationSort()),
                        Aggregates.limit(Constants.HALL_OF_FAME_ELEMENT_NUMBERS)
                )
        );
        ArrayList<HallOfFameDTO> resultSet = new ArrayList<>();
        HallOfFameDTO hallOfFameDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            hallOfFameDTO =new HallOfFameDTO();
            hallOfFameDTO.setSubject(doc.getInteger("_id").toString());
            hallOfFameDTO.setTop_critic_rating(doc.getDouble("top_critic_rating"));
            hallOfFameDTO.setUser_rating(doc.getDouble("user_rating"));
            hallOfFameDTO.setMovie_count(doc.getInteger("count"));
            resultSet.add(hallOfFameDTO);
        }
        return resultSet;
    }

    /**
     * <method>getYearAndMonthReviewActivity</method> implement the aggregation for finding the user review activities
     * @return an ArrayList of YearMonthReviewDTO
     * @param id is the user id
     * @throws DAOException
     */
    public ArrayList<YearMonthReviewDTO> getYearAndMonthReviewActivity(ObjectId id) {
        MongoCollection<Document>  collection = getMovieCollection();
        Document yearDoc = new Document("year",new Document("$year","$review.review_date"));
        Document monthDoc = new Document("month",new Document("$month","$review.review_date"));
        ArrayList<Document> test=new ArrayList<>();
        test.add(yearDoc);
        test.add(monthDoc);
        AggregateIterable<Document> aggregateResult = collection.aggregate(
                Arrays.asList(
                        Aggregates.match(eq("_id",id)),
                        Aggregates.unwind("$review"),
                        Aggregates.group(test,
                                sum("count",1)),
                        Aggregates.sort(Sorts.ascending("_id"))
                )
        );
        ArrayList<YearMonthReviewDTO> resultSet = new ArrayList<>();
        YearMonthReviewDTO yearMonthReviewDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            //System.out.print(doc.toJson());
            Object obj = doc.get("_id");
            yearDoc.clear();
            monthDoc.clear();
            if (obj instanceof ArrayList) {
                ArrayList<?> dboArrayNested = (ArrayList<?>) obj;
                yearDoc= (Document) dboArrayNested.get(0);
                monthDoc= (Document) dboArrayNested.get(1);
                yearMonthReviewDTO = new YearMonthReviewDTO();
                yearMonthReviewDTO.setYear(yearDoc.getInteger("year"));
                yearMonthReviewDTO.setMonth(monthDoc.getInteger("month"));
                yearMonthReviewDTO.setCount(doc.getInteger("count"));
                resultSet.add(yearMonthReviewDTO);
            }
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
