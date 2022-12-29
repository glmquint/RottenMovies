package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    /*

   CONTROLLO REVIEW BOMBING IN BASE A STORICO VS PERIODO DEFINITO
   MATCH (m:Movie)<-[r:REVIEWED]-()
   WITH  SUM(CASE WHEN r.date<date("2019-12-01") THEN 1 ELSE 0 END) as PreviousCount,
   100*toFloat(SUM(CASE WHEN r.date<date("2019-12-01") AND r.freshness = true THEN 1 ELSE 0 END))/SUM(CASE WHEN r.date<date("2019-12-01") THEN 1 ELSE 0 END) as PreviousRate,
   SUM(CASE WHEN r.date>=date("2019-12-01") AND r.date<date("2020-01-01") THEN 1 ELSE 0 END) as LaterCount,
   100*toFloat(SUM(CASE WHEN r.date>=date("2019-12-01") AND r.date<date("2020-01-01") AND r.freshness = true THEN 1 ELSE 0 END))/SUM(CASE WHEN r.date>=date("2019-12-01") AND r.date<date("2020-01-01") THEN 1 ELSE 0 END) as LaterRate
   RETURN PreviousCount, PreviousRate, LaterCount, LaterRate

    */
    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month) throws  DAOException{
        MovieReviewBombingDTO reviewBombingList = new MovieReviewBombingDTO();
        if(movie.getPrimaryTitle().isEmpty() || month < 0){
            return reviewBombingList;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDate = LocalDate.now().minusMonths(month).format(formatter);
        LocalDate today = LocalDate.now();
        String todayString = today.format(formatter);
        Session session = driver.session();
        reviewBombingList = session.readTransaction((TransactionWork<MovieReviewBombingDTO>)(tx -> {
            String query = "MATCH (m:Movie{title:$movieTitle})<-[r:REVIEWED]-() " +
                    "WITH SUM(CASE WHEN r.date<date(\""+strDate+"\") THEN 1 ELSE 0 END) as StoricCount, " +
                    "100*toFloat(SUM(CASE WHEN r.date<date(\""+strDate+"\") AND r.freshness = true THEN 1 ELSE 0 END))" +
                    "/SUM(CASE WHEN r.date<date(\""+strDate+"\") THEN 1 ELSE 0 END) as StoricRate, " +
                    "SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") THEN 1 ELSE 0 END) as TargetCount, " +
                    "100*toFloat(SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") AND r.freshness = true THEN 1 ELSE 0 END))" +
                    "/SUM(CASE WHEN r.date>=date(\""+strDate+"\") AND r.date<date(\""+todayString+"\") THEN 1 ELSE 0 END) as TargetRate " +
                    "RETURN StoricCount, StoricRate, TargetCount, TargetRate";
            Result result = tx.run(query, parameters("movieTitle", movie.getPrimaryTitle(),
                    "date", strDate));
            MovieReviewBombingDTO feed = new MovieReviewBombingDTO(
                    movie.getPrimaryTitle(),
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
    /*
        CONTROLLO REVIEW BOMBING IN BASE A DUE PERIODI DEFINITI, PERIODO DI CONTROLLO E CHECK SUI DATI
        MATCH(m:Movie{title:"Joker"})<-[r:REVIEWED]-()
         WITH SUM(CASE WHEN r.date>=date("2019-01-01") AND r.date<date("2020-01-01")  THEN 1 ELSE 0 END) as PreviousCount,
         100*toFloat(SUM(CASE WHEN r.date>=date("2019-01-01") AND r.date<date("2020-01-01") AND r.freshness = true THEN 1 ELSE 0 END))/SUM(CASE WHEN r.date>=date("2019-01-01") AND r.date<date("2020-01-01")  THEN 1 ELSE 0 END) as PreviousRate,
         SUM(CASE WHEN r.date>=date("2020-01-01") AND r.date<date("2021-01-01")  THEN 1 ELSE 0 END) as LaterCount,
          100*toFloat(SUM(CASE WHEN r.date>=date("2020-01-01") AND r.date<date("2021-01-01") AND r.freshness = true THEN 1 ELSE 0 END))/SUM(CASE WHEN r.date>=date("2020-01-01") AND r.date<date("2021-01-01")  THEN 1 ELSE 0 END) as LaterRate
          RETURN PreviousCount, PreviousRate, LaterCount, LaterRate, toFloat(LaterRate)/PreviousRate as totalRate, toFloat(LaterCount)/(PreviousCount) as bs

     */



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
