package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.popFirst;
import static com.mongodb.client.model.Updates.push;

public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {
    private MongoCollection<Document> getMovieCollection(){
        return returnCollection(myClient, Constants.COLLECTION_STRING_MOVIE);
    }
    private MongoCollection<Document> getUserCollection(){
        return returnCollection(myClient, Constants.COLLECTION_STRING_USER);
    }
    private BasicDBObject buildLast3ReviewField(Review r){
        BasicDBObject reviewLast3 = new BasicDBObject();
        reviewLast3.put("_id",r.getMovie_id());
        reviewLast3.put("top_critic",r.isTopCritic());
        reviewLast3.put("primaryTitle",r.getMovie());
        reviewLast3.put("review_type",r.getReviewType());
        reviewLast3.put("review_score",r.getReviewScore());
        reviewLast3.put("review_date",r.getReviewDate());
        reviewLast3.put("review_content",r.getReviewContent());
        return reviewLast3;
    }
    private BasicDBObject buildMovieReviewField(Review r){
        BasicDBObject reviewMovie = new BasicDBObject();
        reviewMovie.put("critic_name",r.getCriticName());
        reviewMovie.put("review_type",r.getReviewType());
        reviewMovie.put("review_score",r.getReviewScore());
        reviewMovie.put("review_date",r.getReviewDate());
        reviewMovie.put("review_content",r.getReviewContent());
        return reviewMovie;
    }
    private BasicDBObject buildSimplyfiedReview (Review r,int index){
        BasicDBObject reviewMovie = new BasicDBObject();
        reviewMovie.put("movie_id",r.getMovie_id());
        reviewMovie.put("primaryTitle",r.getMovie());
        reviewMovie.put("review_index",index);
        return reviewMovie;
    }

    private HashMap<String,Integer> updateMovieRating (Document doc,boolean freshreview){ // utitliy function
        HashMap<String,Integer> map = new HashMap<>();
        Set<String> keyset=doc.keySet();
        for(String s: keyset){
            map.put(s,doc.getInteger(s));
        }
        System.out.println(map);
        return map;

    }

    @Override
    public boolean reviewMovie(BaseUser usr, Review review) throws DAOException{
        // TODO: remember to update the movie status (number of votes, etc)
        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();
        /*
        BasicDBObject reviewLast3 = buildLast3ReviewField(review);
        Bson match = new Document("$match", Filters.eq("_id", review.getMovie_id()));
        Bson project = Aggregates.project(fields(include("tomatometer_rating","audience_rating","tomatometer_fresh_critics_count",
                "tomatometer_rotten_critics_count"), excludeId(),Projections.computed("count",new Document("$size", "$review" ))));
        AggregateIterable res=movieCollection.aggregate(Arrays.asList(match, project));
        MongoCursor<Document> cursor = res.cursor();
        boolean freshReview = (review.getReviewType()=="Fresh");
        while(cursor.hasNext()){
           updateMovieRating(cursor.next(),freshReview);
        }


        if(usr instanceof User){
            // inc audience_count
            //update rating and status
            if(freshReview){

            }
        }
        else { // it's a TopCritic
            if(freshReview){
                // increment tomatometer_fresh_critics_count
            }
            else {
                // increment tomatometer_rotten_critics_count
            }
            // updates tomatomer_rating and status
        }

        review.setCriticName(usr.getUsername());
        BasicDBObject reviewMovie = buildMovieReviewField(review);
        Bson filterMovie = eq("_id",  review.getMovie_id());
        Bson filterUsr = eq("_id", usr.getId());
        Bson updateMovie = push("review", reviewMovie);
        movieCollection.updateOne(filterMovie,updateMovie);
        BasicDBObject simplyfiedreview = buildSimplyfiedReview(review,elem_array);
        Bson updateMovieUser = push("reviews", simplyfiedreview);
        userCollection.updateOne(filterUsr,updateMovieUser);
        Bson updateUsr = push("last_3_reviews", reviewLast3);
        userCollection.updateOne(filterUsr,updateUsr);
        userCollection.updateOne(filterUsr,popFirst("last_3_reviews"));

        */
        return true;
    }

    @Override
    public boolean delete(Review review) throws DAOException{
        if(review.getCriticName()==null){
            return false;
        }
        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();

        Document user=userCollection.find(eq("username", review.getCriticName())).first();
        Bson filterUsr = eq("username", review.getCriticName());
        Bson filterMovie = eq("_id",review.getMovie_id());
        userCollection.updateOne(filterUsr,Updates.pull("last_3_reviews",filterMovie));

        Bson projection = Projections.fields( excludeId(), elemMatch("reviews", eq("primaryTitle", review.getMovie())));
        Document doc= userCollection.find(filterUsr).projection(projection).first();
        Object obj = doc.get("reviews");
        Document doc2 = null;
        if (obj instanceof ArrayList) {
            ArrayList<?> dboArrayNested = (ArrayList<?>) obj;
            for (Object dboNestedObj : dboArrayNested) {
                if (dboNestedObj instanceof Document) {
                    doc2=Document.class.cast(dboNestedObj);
                }
            }
        }
        else {
            return false;
        }

        int index=doc2.getInteger("review_index");
        Document set=new Document("$set", new Document("review."+index+".critic_name",Constants.DELETED_REVIEW));
        movieCollection.updateOne(filterMovie, set);
        Bson updates = Updates.combine(Updates.pull("reviews",eq("movie_id",review.getMovie_id())));
        userCollection.updateOne(filterUsr,updates);
        return true;
    }
    @Override
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException {
        throw new DAOException("method not implemented for Mongo DB");
    }

    /*
    ==========================================================================
    INSERIRE SOPRA I METODI DI MONGO E SOTTO QUELLI DI NEO4J
    ==========================================================================
     */
    public boolean reviewMovieNeo4j(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }

    public boolean deleteReviewNeo4j(String userId, String movieId) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
