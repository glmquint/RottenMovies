package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

import it.unipi.dii.lsmsdb.rottenMovies.models.*;

import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.popFirst;
import static com.mongodb.client.model.Updates.push;

/**
 * <class>ReviewMongoDB_DAO</class> is responsible for the review-field operation in
 * the MongoDB
 */
public class ReviewMongoDB_DAO extends BaseMongoDAO implements ReviewDAO {

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
        reviewMovie.put("top_critic",r.isTopCritic());
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
    /**
     * <method>reviewMovie</method> add the user review in a movie
     * @param usr
     * @param review
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
    @Override
    public boolean reviewMovie(BaseUser usr, Review review) {
        if(usr.getId()==null || review.getMovie_id()==null){
            System.out.println("ReviewMongoDB_DAO.reviewMovie[ERROR]:review fields cannot be null values! Check user_id,movie_id");
            return false;
        }

        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();

        boolean freshReview = (review.getReviewType().equals("Fresh"));
        BasicDBObject reviewLast3 = buildLast3ReviewField(review);
        Bson movieFilter = eq("_id", review.getMovie_id());

        int newReviewIndex=updateMovieRating(movieCollection,movieFilter,(usr instanceof TopCritic),freshReview,true);

        review.setCriticName(usr.getUsername());
        BasicDBObject reviewMovie = buildMovieReviewField(review);
        Bson updateMovie = push("review", reviewMovie);
        Bson filterUsr = eq("_id", usr.getId());
        BasicDBObject simplyfiedReview = buildSimplyfiedReview(review,newReviewIndex);
        Document numberOfElementLast3 = userCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(eq("_id",usr.getId())),
                        Aggregates.project(new Document("count",new Document("$size","$last_3_reviews")))
                )
        ).first();
        try{
            UpdateResult result=movieCollection.updateOne(movieFilter,updateMovie);
            System.out.println("Modified document count: " + result.getModifiedCount());

            Bson updateMovieUser = push("reviews", simplyfiedReview);
            result=userCollection.updateOne(filterUsr,updateMovieUser);
            System.out.println("Modified document count: " + result.getModifiedCount());

            Bson updateUsr = push("last_3_reviews", reviewLast3);
            result=userCollection.updateOne(filterUsr,updateUsr);
            System.out.println("Modified document count: " + result.getModifiedCount());
            for(int count=numberOfElementLast3.getInteger("count"); count>=3; count--) {
                result = userCollection.updateOne(filterUsr, popFirst("last_3_reviews"));
                if(result.getModifiedCount()!=1) {
                    System.out.println("Last3ReviewPopERROR!");
                    break;
                }
            }
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
        return true;
    }
    private int calculateRating(int fresh, int rotten){
        double div = ((double)fresh/((double) fresh+(double) rotten));
        return (int) ((div*100)+0.5);
    }
    private String calculateTopCriticStatus(int critic_rating, int totalReviewCount, int critic_fresh, int critic_rotten){
        return (critic_rating>=60)?((critic_rating>=75 && (totalReviewCount)>=80 && (critic_fresh+critic_rotten)>=5)?"Certified Fresh":"Fresh"):"Rotten";
    }

    /**
     * <method>updateMovieRating</method> update the movie rating based on the review
     * @param movieCollection, movieFilter, isTop, isFresh, isNew
     * @return number of reviews made
     * @throws DAOException
     */

    private int updateMovieRating (MongoCollection<Document> movieCollection,Bson movieFilter,boolean isTop,boolean isFresh,boolean isNew){
        Bson projectfield=Projections.fields(excludeId(),include("user_fresh_count","user_rotten_count","top_critic_rotten_count","top_critic_fresh_count","user_status","top_critic_status"));
        Document res = movieCollection.find(movieFilter).projection(projectfield).first();
        int user_fresh=res.getInteger("user_fresh_count");
        int user_rotten=res.getInteger("user_rotten_count");
        int critic_fresh=res.getInteger("top_critic_fresh_count");
        int critic_rotten=res.getInteger("top_critic_rotten_count");
        String user_status=res.getString("user_status");
        String top_critic_status=res.getString("top_critic_status");
        int totalReviewCount=critic_fresh+critic_rotten+user_fresh+user_rotten;
        Bson updates=null;
        if(!isTop){ // it is a user review
            if(isFresh){
                user_fresh++;
                updates=Updates.set("user_fresh_count",user_fresh);
                if(!isNew) { // it's an old review
                    user_rotten--;
                    updates = Updates.combine(updates, Updates.set("user_rotten_count", user_rotten));
                }
            }
            else { // it is a rotten review
                user_rotten++;
                updates=Updates.set("user_rotten_count",user_rotten);
                if(!isNew) { // it's an old review
                    user_fresh--;
                    updates=Updates.combine(updates,Updates.set("user_fresh_count",user_fresh));
                }
            }
            int user_rating= calculateRating(user_fresh,user_rotten);
            String status=(user_rating>=60)?"Upright":"Spilled";
            if(!status.equals(user_status)){
                updates=Updates.combine(updates,Updates.set("user_status",status));
            }
            updates = Updates.combine(updates,Updates.set("user_rating",user_rating));
        }
        else { // it's a TopCritic
            if(isFresh){
                critic_fresh++;
                updates=Updates.set("top_critic_fresh_count",critic_fresh);
                if(!isNew){ // it's an old review
                    critic_rotten--;
                    updates=Updates.combine(updates,Updates.set("top_critic_rotten_count",critic_rotten));
                }
            }
            else {
                critic_rotten++;
                updates=Updates.set("top_critic_rotten_count",critic_rotten);
                if(!isNew) { // it's an old review
                    critic_fresh--;
                    updates = Updates.combine(updates,Updates.set("top_critic_fresh_count", critic_fresh));
                }
            }
            int critic_rating= calculateRating(critic_fresh,critic_rotten);
            String critic_status = calculateTopCriticStatus(critic_rating,totalReviewCount,critic_fresh,critic_rotten);
            if(!top_critic_status.equals(critic_status)){
                updates=Updates.combine(updates,Updates.set("top_critic_status",critic_status));
            }
            updates = Updates.combine(updates, Updates.set("top_critic_rating",critic_rating));
        }
        try{
            UpdateResult result=movieCollection.updateOne(movieFilter,updates);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
        return totalReviewCount;
    }

    /**
     * <method>delete</method> deletes a review both in movie and user collections
     * @param review is the model
     * @return true in case of success, false otherwise
     * @throws DAOException
     */
    @Override
    public boolean delete(Review review) {
        if(review.getCriticName()==null || review.getMovie_id()==null || review.getMovie()==null){
            System.out.println("ReviewMongoDB_DAO.delete[ERROR]:review fields cannot be null values! Check critic_name,movie_id and primaryTitle");
            return false;
        }
        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();

        Bson filterUsr = eq("username", review.getCriticName());
        Bson filterMovie = eq("_id",review.getMovie_id());

        Bson projection = Projections.fields( excludeId(), elemMatch("reviews", eq("primaryTitle", review.getMovie())));
        Document doc= userCollection.find(filterUsr).projection(projection).first();
        Object obj = doc.get("reviews");
        Document doc2 = null;
        if (obj instanceof ArrayList) {
            ArrayList<?> dboArrayNested = (ArrayList<?>) obj;
            Object dboNestedObj = dboArrayNested.get(0);
                if (dboNestedObj instanceof Document) {
                    doc2= (Document) dboNestedObj;
                }
                else
                    return false;
        }
        else {
            return false;
        }
        int index=doc2.getInteger("review_index");
        try{
            UpdateResult result=userCollection.updateOne(filterUsr,Updates.pull("last_3_reviews",filterMovie));
            System.out.println("Modified document count: " + result.getModifiedCount());

            Document set1=new Document("$set", new Document("review."+index+".critic_name",Constants.DELETED_REVIEW));
            Document set2=new Document("$set", new Document("review."+index+".review_content",""));
            Document set3=new Document("$set", new Document("review."+index+".review_score",""));

            result=movieCollection.updateOne(filterMovie, Updates.combine(set1, set2, set3));
            System.out.println("Modified document count: " + result.getModifiedCount());

            Bson updates = Updates.combine(Updates.pull("reviews",eq("movie_id",review.getMovie_id())));
            result=userCollection.updateOne(filterUsr,updates);
            System.out.println("Modified document count: " + result.getModifiedCount());
        }
        catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
        return true;
    }

    /**
     * <method>update</method> update a review that a user made
     * @param usr is the user model, reviewUpdated is the review model
     * @return true in case of success, false otherwise
     * @throws DAOException
     */

    public boolean update (BaseUser usr,Review reviewUpdated){
        if(reviewUpdated.getMovie_id()==null || usr.getId()==null){
            System.out.println("ReviewMongoDB_DAO.update[ERROR]:review fields cannot be null values! Check movie_id and user_id");
            return false;
        }
        MongoCollection<Document> userCollection = getUserCollection();
        MongoCollection<Document> movieCollection = getMovieCollection();

        ArrayList<Review> last3 = usr.getLast3Reviews();

        boolean needsRatingUpdate=false;
        HashMap<String,String> changedFields = new HashMap<>();
        int index=0;
        for(Review r:last3){ // check if the reviews to be modified is in last_3
            if(r.getMovie_id().equals(reviewUpdated.getMovie_id())){
                index=last3.indexOf(r);
                if(!r.getReviewType().equals(reviewUpdated.getReviewType())) {
                    needsRatingUpdate = true;
                    changedFields.put(".review_type", reviewUpdated.getReviewType());
                }
                if(!r.getReviewScore().equals(reviewUpdated.getReviewScore()))
                    changedFields.put(".review_score",reviewUpdated.getReviewScore());
                if(!r.getReviewContent().equals(reviewUpdated.getReviewContent()))
                    changedFields.put(".review_content",reviewUpdated.getReviewContent());
                break;
            }

        }
        Bson updates=null;
        if (!changedFields.isEmpty()){ // update last_3_review field if needed
            updates = Updates.set("last_3_reviews."+index+".review_date", reviewUpdated.getReviewDate());
            for(String k:changedFields.keySet()) {
                updates=Updates.combine(updates,Updates.set("last_3_reviews."+index+k,changedFields.get(k)));
            }
            try {
                UpdateResult result=userCollection.updateOne(eq("_id", usr.getId()), updates);
                System.out.println("Modified document count: " + result.getModifiedCount());
            }
            catch (MongoException me){
                System.err.println("Unable to update due to an error: " + me);
                return false;
            }
        }

        ArrayList<SimplyfiedReview> listrev = usr.getReviews();
        boolean found=false;
        for(SimplyfiedReview s:listrev){ // check the presence in all reviews
            if(s.getMovieID().equals(reviewUpdated.getMovie_id())){
                index=s.getIndex();
                found=true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        Bson movieFilter = eq("_id", reviewUpdated.getMovie_id());
        Movie m=null;
        Document dbquery = movieCollection.find(movieFilter).projection(Projections.slice("review",index,1)).first();
        ObjectMapper mapper = new ObjectMapper();
        try {
            m = mapper.readValue(dbquery.toJson(), Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Review revdb=m.getReviews().get(0);
        changedFields.clear();
        if(!revdb.getReviewType().equals(reviewUpdated.getReviewType())) {
            needsRatingUpdate = true;
            changedFields.put(".review_type", reviewUpdated.getReviewType());
        }
        if(!revdb.getReviewScore().equals(reviewUpdated.getReviewScore()))
            changedFields.put(".review_score",reviewUpdated.getReviewScore());
        if(!revdb.getReviewContent().equals(reviewUpdated.getReviewContent()))
            changedFields.put(".review_content",reviewUpdated.getReviewContent());

        updates=null;
        if(needsRatingUpdate){
            updateMovieRating(movieCollection,movieFilter,(usr instanceof TopCritic),(reviewUpdated.getReviewType().equals("Fresh")),false);
        }
        if(!changedFields.isEmpty()) { // update the review in movie collection
            updates = Updates.set("review." + index + ".review_date", reviewUpdated.getReviewDate());
            for (String k : changedFields.keySet()) {
                updates = Updates.combine(updates, Updates.set("review." + index + k, changedFields.get(k)));
            }
            try {
                UpdateResult result=movieCollection.updateOne(movieFilter, updates);
                System.out.println("Modified document count: " + result.getModifiedCount());
            }
            catch (MongoException me){
                System.err.println("Unable to update due to an error: " + me);
                return false;
            }
        }
        return true;
    }

    /**
     * <method>getIndexOfReview</method> return the index of the review made by user
     * @param userid is the user Object Id, primaryTitle is the movie's primary title
     * @return an ArrayList of Object (movie id and the review index)
     * @throws DAOException
     */
    @Override
    public ArrayList<Object> getIndexOfReview(ObjectId userid, String primaryTitle) {
        MongoCollection <Document> userCollection = getUserCollection();
        ArrayList<Object> movieAndIndex = new ArrayList<>();
        Bson projection = Projections.fields( excludeId(), elemMatch("reviews", eq("primaryTitle", primaryTitle)));
        Document doc= userCollection.find(eq("_id",userid)).projection(projection).first();
        Object obj = doc.get("reviews");
        Document doc2 = null;
        if (obj instanceof ArrayList) {
            ArrayList<?> dboArrayNested = (ArrayList<?>) obj;
            Object dboNestedObj = dboArrayNested.get(0);
            if (dboNestedObj instanceof Document) {
                doc2= (Document) dboNestedObj;
            }
            else
                return movieAndIndex;
        }
        else {
            return movieAndIndex;
        }
        movieAndIndex.add(doc2.get("movie_id"));
        movieAndIndex.add(doc2.getInteger("review_index"));
        return movieAndIndex;
    }

    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
