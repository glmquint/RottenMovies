package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import org.bson.types.ObjectId;
import org.neo4j.driver.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.NEO4J_DATABASE_STRING;
import static org.neo4j.driver.Values.parameters;
/**
 * <class>ReviewNeo4j_DAO</class> allow to use methods to interact with the GraphDB
 * specifically for the REVIEWED relationship
 */
public class ReviewNeo4j_DAO extends BaseNeo4jDAO implements ReviewDAO {

    /**
     * <method>reviewMovie</method> create a REVIEWED relationship between a User/TopCritic and a Movie
     * @param usr is the User/TopCritic who wrote the review
     * @param review is the Review written by usr
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean reviewMovie(BaseUser usr, Review review)  throws DAOException{
        if(usr.getId().toString().isEmpty() || review.getMovie().isEmpty() || review.getReviewDate()==null){
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(review.getReviewDate());
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        boolean freshness = (review.getReviewType().equals("Fresh")) ? true : false;
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $userId}), " +
                    "(m:Movie{id: $movieId}) " +
                    "MERGE (b)-[r:REVIEWED {content: $content, date: date(\""+strDate+"\"), freshness: $freshness}]->(m)" +
                    "RETURN type(r) as Type, r.date as Date, r.freshness as Freshness";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
                    "movieId", review.getMovie_id().toString(),
                    "content", review.getReviewContent(),
                    "freshness", freshness));
            System.out.println(result.peek().get("Type").asString());
            System.out.println(result.peek().get("Date"));
            System.out.println(result.peek().get("Freshness"));
            return 1;
        });
        return true;
    }
    /**
     * <method>delete</method> delete a REVIEWED relationship between a User/TopCritic and a Movie
     * @param review is the Review written by usr that needs to be deleted
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean delete(Review review)  throws DAOException{
        if(review.getCriticName().isEmpty() || review.getMovie().isEmpty()){
            return false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $user}) -[r:REVIEWED] -> (m:Movie{id: $movieId})" +
                    "DELETE r";
            Result result = tx.run(query, parameters("user", review.getCriticName(),
                    "movieId", review.getMovie_id().toString()));

            return 1;
        });
        return true;
    }

    /**
     * <method>checkReviewBombing</method> check if a particular film was reviewed bombed
     * @param movie is the target movie to check for review bombing
     * @param month is the number of month from today's date in which to check for review bombing
     * @return a MovieReviewBombingDTO used to visualize the result of the query
     * @throws DAOException
     */
    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws  DAOException{
        MovieReviewBombingDTO reviewBombingList = new MovieReviewBombingDTO();
        if(movie.getId()==null || month <= 0){
            return reviewBombingList;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDate = LocalDate.now().minusMonths(month).format(formatter);
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        reviewBombingList = session.readTransaction((TransactionWork<MovieReviewBombingDTO>)(tx -> {
            String query = "MATCH (m:Movie{id:$movieId})<-[r:REVIEWED]-() " +
                    "WITH SUM(CASE WHEN r.date<date(\""+strDate+"\") THEN 1 ELSE 0 END) as StoricCount, " +
                    "100*toFloat(SUM(CASE WHEN r.date<date(\""+strDate+"\") AND r.freshness = true THEN 1 ELSE 0 END))" +
                    "/SUM(CASE WHEN r.date<date(\""+strDate+"\") THEN 1 ELSE 0 END) as StoricRate, " +
                    "SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") THEN 1 ELSE 0 END) as TargetCount, " +
                    "100*toFloat(SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") AND r.freshness = true THEN 1 ELSE 0 END))" +
                    "/SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") THEN 1 ELSE 0 END) as TargetRate, m.title as Title " +
                    "RETURN Title, StoricCount, StoricRate, TargetCount, TargetRate";
            Result result=null;
            try{
                result=tx.run(query, parameters("movieId", movie.getId().toString(),
                        "date", strDate));
            }
            catch (org.neo4j.driver.exceptions.ClientException e){
                return null;
            }
            MovieReviewBombingDTO feed = new MovieReviewBombingDTO(
                    result.peek().get("Title").asString(),
                    result.peek().get("StoricCount").asInt(),
                    (int)result.peek().get("StoricRate").asDouble(),
                    result.peek().get("TargetCount").asInt(),
                    (int)result.peek().get("TargetRate").asDouble(),
                    LocalDate.now().minusMonths(month)
            );
            return feed;
        }));
        return reviewBombingList;
    }

    /**
     * <method>update</method> update a REVIEWED relationship in the graph db
     * @param usr is the model of the user who wrote the review
     * @param review is the review written by the user
     * @return true if the transaction concluded successfully
     * @throws DAOException
     */
    public boolean update(BaseUser usr, Review review) throws DAOException {
        if(usr.getId().toString().isEmpty() ||review.getMovie_id()==null || review.getReviewContent().isEmpty() || review.getReviewDate()==null){
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(review.getReviewDate());
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        boolean freshness = (review.getReviewType().equals("Fresh")) ? true : false;
        session.writeTransaction(tx -> {
            String query = "MATCH (u{id: $userId})-[r:REVIEWED]->(m:Movie{id: $movieId}) " +
                    "SET r.content = $content, " +
                    "r.date = date(\"" + strDate + "\"), " +
                    "r.freshness = $freshness";
            Result result = tx.run(query, parameters("userId", usr.getId().toString(),
                    "movieId", review.getMovie_id().toString(),
                    "content", review.getReviewContent(),
                    "freshness", freshness));
            return 1;
        });
        return true;
    }

    public boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<Object> getIndexOfReview(ObjectId userid, String primaryTitle) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

}
