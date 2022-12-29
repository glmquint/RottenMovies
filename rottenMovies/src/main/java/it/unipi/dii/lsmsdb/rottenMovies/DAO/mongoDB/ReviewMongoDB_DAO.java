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
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;
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

    @Override
    public boolean reviewMovie(BaseUser usr, Review review) throws DAOException{
        if(usr.getId()==null || review.getMovie_id()==null){
            System.out.println("ReviewMongoDB_DAO.reviewMovie[ERROR]:review fields cannot be null values! Check user_id,movie_id");
            return false;
        }
        
        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();

        boolean freshReview = (review.getReviewType()=="Fresh");
        BasicDBObject reviewLast3 = buildLast3ReviewField(review);
        Bson movieFilter = eq("_id", review.getMovie_id());
        /*
        Bson match = new Document("$match", Filters.eq("_id", review.getMovie_id()));
        Bson project = Aggregates.project(fields(include("tomatometer_rating","audience_rating","tomatometer_fresh_critics_count",
                "tomatometer_rotten_critics_count"), excludeId(),Projections.computed("count",new Document("$size", "$review" ))));
        AggregateIterable res=movieCollection.aggregate(Arrays.asList(match, project));
        MongoCursor<Document> cursor = res.cursor();
        while(cursor.hasNext()){
           updateMovieRating(cursor.next(),freshReview);
        }
        */
        Bson projectfield=Projections.fields(excludeId(),include("user_fresh_count","user_rotten_count","top_critic_rotten_count","top_critic_fresh_count"));
        Document res = movieCollection.find(movieFilter).projection(projectfield).first();
        int user_fresh=res.getInteger("user_fresh_count");
        int user_rotten=res.getInteger("user_rotten_count");
        int critic_fresh=res.getInteger("top_critic_fresh_count");
        int critic_rotten=res.getInteger("top_critic_rotten_count");
        int elemOrSum=critic_fresh+critic_rotten+user_fresh+user_rotten;

        if(usr instanceof User){
            if(freshReview){
                user_fresh++;
            }
            else {
                user_rotten++;
            }
            Double div = (double)user_fresh/((double) user_fresh+(double) user_rotten);
            int user_rating= (int) (div*100);
            String status=(user_rating>=60)?"Upright":"Spilled";
            Bson updates = Updates.combine(
                    Updates.set("user_fresh_count",user_fresh),
                    Updates.set("user_rotten_count",user_rotten),
                    Updates.set("user_rating",user_rating),
                    Updates.set("user_status",status));
            movieCollection.updateOne(movieFilter,updates);
        }
        else { // it's a TopCritic
            if(freshReview){
                critic_fresh++;
            }
            else {
                critic_rotten++;
            }
            Double div = (double)critic_fresh/((double) critic_fresh+(double) critic_rotten);
            int critic_rating= (int) (div*100);
            String critic_status=(critic_rating>=60)?((critic_rating>=75 && (elemOrSum)>=80 && (critic_fresh+critic_rotten)>=5)?"Certified Fresh":"Fresh"):"Rotten";
            Bson updates = Updates.combine(
                    Updates.set("top_critic_fresh_count",critic_fresh),
                    Updates.set("top_critic_rotten_count",critic_rotten),
                    Updates.set("top_critic_rating",critic_rating),
                    Updates.set("top_critic_status",critic_status));
            movieCollection.updateOne(movieFilter,updates);
        }

        review.setCriticName(usr.getUsername());
        BasicDBObject reviewMovie = buildMovieReviewField(review);
        Bson updateMovie = push("review", reviewMovie);
        movieCollection.updateOne(movieFilter,updateMovie);
        Bson filterUsr = eq("_id", usr.getId());
        BasicDBObject simplyfiedreview = buildSimplyfiedReview(review,elemOrSum);
        Bson updateMovieUser = push("reviews", simplyfiedreview);
        userCollection.updateOne(filterUsr,updateMovieUser);
        Bson updateUsr = push("last_3_reviews", reviewLast3);
        userCollection.updateOne(filterUsr,updateUsr);
        userCollection.updateOne(filterUsr,popFirst("last_3_reviews"));
        return true;
    }

    @Override
    public boolean delete(Review review) {
        if(review.getCriticName()==null || review.getMovie_id()==null || review.getMovie()==null){
            System.out.println("ReviewMongoDB_DAO.delete[ERROR]:review fields cannot be null values! Check critic_name,movie_id and primaryTitle");
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
    public boolean update (BaseUser usr,Review reviewUpdated){
        MongoCollection<Document> userCollection = getUserCollection();
        MongoCollection<Document> movieCollection = getMovieCollection();

        if(reviewUpdated.getMovie_id()==null || usr.getId()==null){
            System.out.println("ReviewMongoDB_DAO.update[ERROR]:review fields cannot be null values! Check movie_id and user_id");
            return false;
        }
        ArrayList<Review> last3 = usr.getLast3Reviews();
        boolean found=false;
        int index=0;
        for(Review r:last3){
            if(r.getMovie_id().equals(reviewUpdated.getMovie_id())){
                index=last3.indexOf(r);
                found=true;
                break;
            }
        }
        if (found){
            Bson updates = Updates.combine(
                    Updates.set("last_3_reviews."+index+".review_type",reviewUpdated.getReviewType()),
                    Updates.set("last_3_reviews."+index+".review_score", reviewUpdated.getReviewScore()),
                    Updates.set("last_3_reviews."+index+".review_date", reviewUpdated.getReviewDate()),
                    Updates.set("last_3_reviews."+index+".review_content", reviewUpdated.getReviewContent()));
            userCollection.updateOne(eq("_id",usr.getId()),updates);
        }
        ArrayList<SimplyfiedReview> listrev = usr.getReviews();
        found=false;
        for(SimplyfiedReview s:listrev){
            if(s.getMovieID().equals(reviewUpdated.getMovie_id())){
                index=s.getIndex();
                found=true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        Bson updates = Updates.combine(
                Updates.set("review." + index + ".review_type", reviewUpdated.getReviewType()),
                Updates.set("review." + index + ".review_score", reviewUpdated.getReviewScore()),
                Updates.set("review." + index + ".review_date", reviewUpdated.getReviewDate()),
                Updates.set("review." + index + ".review_content", reviewUpdated.getReviewContent()));
        movieCollection.updateOne(eq("_id", reviewUpdated.getMovie_id()), updates);
        // TODO: update the rating in movie, maybe you can use find one and update
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
