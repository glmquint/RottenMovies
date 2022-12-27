package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.REVIEWS_IN_FEED;
import static org.neo4j.driver.Values.parameters;

public class ReviewNeo4j_DAO extends BaseNeo4jDAO implements ReviewDAO {
    @Override
    public boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public boolean reviewMovie(BaseUser usr, Review review)  throws DAOException{

        if(usr.getId().toString().isEmpty() ||review.getMovie().isEmpty() || review.getReviewContent().isEmpty() || review.getReviewDate()==null){
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(review.getReviewDate());
        Session session = driver.session();
        boolean freshness = (review.getReviewType().equals("Fresh")) ? true : false;
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $userId}), " +
                    "(m:Movie{title: $movieTitle}) " +
                    "MERGE (b)-[r:REVIEWED {content: $content, date: date(\""+strDate+"\"), freshness: $freshness}]->(m)" +
                    "RETURN type(r) as Type, r.date as Date, r.freshness as Freshness";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
                    "movieTitle", review.getMovie(),
                    "content", review.getReviewContent(),
                    "freshness", freshness));
            System.out.println(result.peek().get("Type").asString());
            System.out.println(result.peek().get("Date"));
            System.out.println(result.peek().get("Freshness"));
            return 1;
        });
        return true;
        /*try{
            reviewMovieNeo4j(usr.getId().toString(), review.getMovie(), review.getReviewContent(), review.getReviewDate(), (review.getReviewType().equals("Fresh")) ? true : false);
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return false;
        */
    }

    @Override
    public boolean delete(Review review)  throws DAOException{
        if(review.getCriticName().isEmpty() || review.getMovie().isEmpty()){
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (b{name: $user}) -[r:REVIEWED] -> (m:Movie{title: $movie})" +
                    "DELETE r";
            Result result = tx.run(query, parameters("user", review.getCriticName(),
                    "movie", review.getMovie()));

            return 1;
        });
        return true;
        /*try{
            deleteReviewNeo4j(review.getCriticName(), review.getMovie());
        } catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;

         */
    }

    @Override
    public ArrayList<ReviewFeedDTO> getFeed(BaseUser usr, int page) throws DAOException {
        if(usr.getId().toString().isEmpty() || page<0){
            return null;
        }
        int skip = page*REVIEWS_IN_FEED;

        Session session = driver.session();
        ArrayList<ReviewFeedDTO> reviewFeed = session.readTransaction((TransactionWork<ArrayList<ReviewFeedDTO>>)(tx -> {
            String query = "MATCH(u:User{id:$userId})-[f:FOLLOWS]->(t:TopCritic)-[r:REVIEWED]->(m:Movie) "+
                    "RETURN m.title AS movieTitle,t.name AS criticName, r.date AS reviewDate, "+
                    "r.content AS content, r.freshness AS freshness " +
                    "ORDER BY r.date DESC SKIP $skip LIMIT $limit ";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
                    "skip", skip, "limit", REVIEWS_IN_FEED));
            ArrayList<ReviewFeedDTO> feed = new ArrayList<>();
            while(result.hasNext()){
                Record r = result.next();
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(r.get("reviewDate").asLocalDate()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                feed.add(new ReviewFeedDTO(
                        r.get("movieTitle").asString(),
                        r.get("criticName").asString(),
                        r.get("content").asString(),
                        r.get("freshness").asBoolean(),
                        date
                ));
            }
            return feed;
        }));
        return reviewFeed;
        /*
        ArrayList<ReviewFeedDTO> reviewList = new ArrayList<>();
        try{
            reviewList = constructFeedNeo4j(usr.getId().toString(), page);
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return reviewList;

         */
    }

    /*
        MATCH (u:User{name:"Dennis Schwartz"})-[r:REVIEWED]->(m:Movie)<-[r2:REVIEWED]-(t:TopCritic)
        WHERE NOT (u)-[:FOLLOWS]->(t)
        RETURN 100*toFloat( sum(case when r.freshness = r2.freshness then 1 else 0 end)+1)/ (count(m.title)+2) as perc,
        t.name as name ORDER by perc DESC LIMIT 10


        MATCH (u:User{name:"Dennis Schwartz"})-[r:REVIEWED]->(m:Movie)<-[r2:REVIEWED]-(t:TopCritic)
        WHERE NOT (u)-[:FOLLOWS]->(t)
        RETURN 100*toFloat( sum(case when r.freshness = r2.freshness then 1 else 0 end)+1)/ (count(m.title)+2) as perc,
        t.name as name, collect(m.title) as movies, collect(r.freshness=r2.freshness) as alignement ORDER by perc DESC LIMIT 20
     */
}
