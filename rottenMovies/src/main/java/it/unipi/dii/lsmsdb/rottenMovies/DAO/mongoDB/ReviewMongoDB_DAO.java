package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;

import it.unipi.dii.lsmsdb.rottenMovies.models.*;

import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;



import java.util.ArrayList;
import java.util.Date;

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
    private int calcolateRating(int fresh,int rotten){
        Double div = (double)fresh/((double) fresh+(double) rotten);
        return (int) (div*100);
    }
    private String calcolateTopCriticStatus(int critic_rating,int elemIndexOrSum,int critic_fresh,int critic_rotten){
        return (critic_rating>=60)?((critic_rating>=75 && (elemIndexOrSum)>=80 && (critic_fresh+critic_rotten)>=5)?"Certified Fresh":"Fresh"):"Rotten";
    }

    private int updateMovieRating (MongoCollection<Document> movieCollection,Bson movieFilter,boolean user,boolean freshReview){
        Bson projectfield=Projections.fields(excludeId(),include("user_fresh_count","user_rotten_count","top_critic_rotten_count","top_critic_fresh_count"));
        Document res = movieCollection.find(movieFilter).projection(projectfield).first();
        int user_fresh=res.getInteger("user_fresh_count");
        int user_rotten=res.getInteger("user_rotten_count");
        int critic_fresh=res.getInteger("top_critic_fresh_count");
        int critic_rotten=res.getInteger("top_critic_rotten_count");
        int elemIndexOrSum=critic_fresh+critic_rotten+user_fresh+user_rotten;

        if(user){
            if(freshReview){
                user_fresh++;
            }
            else {
                user_rotten++;
            }
            int user_rating=calcolateRating(user_fresh,user_rotten);
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
            int critic_rating=calcolateRating(critic_fresh,critic_rotten);
            String critic_status = calcolateTopCriticStatus(critic_rating,elemIndexOrSum,critic_fresh,critic_rotten);
            Bson updates = Updates.combine(
                    Updates.set("top_critic_fresh_count",critic_fresh),
                    Updates.set("top_critic_rotten_count",critic_rotten),
                    Updates.set("top_critic_rating",critic_rating),
                    Updates.set("top_critic_status",critic_status));
            movieCollection.updateOne(movieFilter,updates);
        }
        return elemIndexOrSum;
    }
    @Override
    public boolean reviewMovie(BaseUser usr, Review review) {
        if(usr.getId()==null || review.getMovie_id()==null){
            System.out.println("ReviewMongoDB_DAO.reviewMovie[ERROR]:review fields cannot be null values! Check user_id,movie_id");
            return false;
        }

        MongoCollection<Document> movieCollection = getMovieCollection();
        MongoCollection<Document> userCollection = getUserCollection();

        boolean freshReview = (review.getReviewType()=="Fresh");
        BasicDBObject reviewLast3 = buildLast3ReviewField(review);
        Bson movieFilter = eq("_id", review.getMovie_id());

        int elemOrSum=updateMovieRating(movieCollection,movieFilter,(usr instanceof User),freshReview);
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
        boolean needsRatingUpdate=true;
        int index=0;
        for(Review r:last3){
            if(r.getMovie_id().equals(reviewUpdated.getMovie_id())){
                index=last3.indexOf(r);
                if(r.getReviewType().equals(reviewUpdated.getReviewType()))
                    needsRatingUpdate=false;
                found=true;
                break;
            }

        }
        if (found){
            Bson updates = Updates.combine(
                    Updates.set("last_3_reviews."+index+".review_score", reviewUpdated.getReviewScore()),
                    Updates.set("last_3_reviews."+index+".review_date", reviewUpdated.getReviewDate()),
                    Updates.set("last_3_reviews."+index+".review_content", reviewUpdated.getReviewContent()));
            if(needsRatingUpdate)
                updates=Updates.combine(updates,Updates.set("last_3_reviews."+index+".review_type",reviewUpdated.getReviewType()));
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
        Bson movieFilter = eq("_id", reviewUpdated.getMovie_id());
        if(!needsRatingUpdate){
            Bson updates = Updates.combine(
                    Updates.set("review." + index + ".review_score", reviewUpdated.getReviewScore()),
                    Updates.set("review." + index + ".review_date", reviewUpdated.getReviewDate()),
                    Updates.set("review." + index + ".review_content", reviewUpdated.getReviewContent()));
            movieCollection.updateOne(movieFilter, updates);
            return true;
        }
        else {

            Movie m=null;
            Document dbquery = movieCollection.find(movieFilter).projection(Projections.slice("review",index,1)).first();
            ObjectMapper mapper = new ObjectMapper();
            try {
                m = mapper.readValue(dbquery.toJson(), Movie.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Review r=m.getReviews().get(0);
            Bson updates = Updates.combine(
                    Updates.set("review." + index + ".review_score", reviewUpdated.getReviewScore()),
                    Updates.set("review." + index + ".review_date", reviewUpdated.getReviewDate()),
                    Updates.set("review." + index + ".review_content", reviewUpdated.getReviewContent()));

            if(r.getReviewType()!=reviewUpdated.getReviewType()){
                int one = (reviewUpdated.getReviewType()=="Rotten")?-1:1;
                    if(usr instanceof BaseUser){
                        m.setUser_fresh_count((m.getUser_fresh_count())+one);
                        m.setUser_rotten_count((m.getUser_rotten_count())-one);
                        m.setUser_rating(calcolateRating(m.getUser_fresh_count(),m.getUser_rotten_count()));
                        m.setUser_status(m.getUser_rating()>=60?"Fresh":"Rotten");
                        updates=Updates.combine(updates,
                                Updates.set("user_fresh_count",m.getUser_fresh_count()),
                                Updates.set("user_rotten_count",m.getUser_rotten_count()),
                                Updates.set("user_rating",m.getUser_rating()),
                                Updates.set("user_status",m.getUser_status()));
                    }
                    else {
                        m.setTop_critic_fresh_count((m.getTop_critic_fresh_count())+one);
                        m.setTop_critic_rotten_count((m.getTop_critic_rotten_count())-one);
                        m.setTop_critic_rating(calcolateRating(m.getTop_critic_fresh_count(),m.getTop_critic_rotten_count()));
                        int sum=m.getTop_critic_rotten_count()+m.getTop_critic_fresh_count()+m.getUser_fresh_count()+m.getUser_rotten_count();
                        m.setTop_critic_status(calcolateTopCriticStatus(m.getTop_critic_rating(),sum,m.getTop_critic_fresh_count(),m.getTop_critic_rotten_count()));
                        updates=Updates.combine(updates,
                                Updates.set("top_critic_fresh_count",m.getTop_critic_fresh_count()),
                                Updates.set("top_critic_rotten_count",m.getTop_critic_rotten_count()),
                                Updates.set("top_critic_rating",m.getTop_critic_rating()),
                                Updates.set("top_critic_status",m.getTop_critic_status()));
                    }
                    updates=Updates.combine(updates,Updates.set("review."+index+".review_type",reviewUpdated.getReviewType()));
                }
                movieCollection.updateOne(movieFilter, updates);
                return true;
            }
    }

    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws DAOException{
        throw new DAOException("requested a query for the Neo4j DB in the MongoDB connection");
    }
}
