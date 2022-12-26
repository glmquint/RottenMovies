package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewFeedDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
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
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public boolean reviewMovieNeo4j(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException{
        if(userId.isEmpty() ||movieId.isEmpty() || content.isEmpty() || date==null){
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $userId}), " +
                    "(m:Movie{id: $movieId}) " +
                    "MERGE (b)-[r:REVIEWED {content: $content, date: date(\""+strDate+"\"), freshness: $freshness}]->(m)" +
                    "RETURN type(r) as Type, r.date as Date, r.freshness as Freshness";
            Result result = tx.run(query, parameters("userId", userId, "movieId", movieId, "content", content, "freshness", freshness));
            System.out.println(result.peek().get("Type").asString());
            System.out.println(result.peek().get("Date"));
            System.out.println(result.single().get("Freshness"));
            return 1;
        });
        return true;
    }

    public boolean deleteReviewNeo4j(String userId, String movieId) throws DAOException{
        if(userId.isEmpty() || movieId.isEmpty()){
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (b{id: $userId}) -[r:REVIEWED] -> (m:Movie{id: $movieId})" +
                    "DELETE r";
            Result result = tx.run(query, parameters("userId", userId, "movieId", movieId));

            return 1;
        });
        return true;
    }

    public List<ReviewFeedDTO> constructFeedNeo4j(String userId, int page) throws DAOException{
        if(userId.isEmpty() || page<0){
            return null;
        }
        int skip = page*REVIEWS_IN_FEED;

        Session session = driver.session();
        List<ReviewFeedDTO> reviewFeed = session.readTransaction((TransactionWork<List<ReviewFeedDTO>>)(tx -> {
            String query = "MATCH(u:User{id:$userId})-[f:FOLLOWS]->(t:TopCritic)-[r:REVIEWED]->(m:Movie) "+
                    "RETURN m.title AS movieTitle,t.name AS criticName, r.date AS reviewDate, "+
                    "r.content AS content, r.freshness AS freshness " +
                    "ORDER BY r.date DESC SKIP $skip LIMIT $limit ";
            Result result = tx.run(query, parameters("userId", userId, "skip", skip, "limit", REVIEWS_IN_FEED));
            List<ReviewFeedDTO> feed = new ArrayList<>();
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
