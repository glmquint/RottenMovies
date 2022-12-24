package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.neo4j.driver.Values.parameters;

public class ReviewNeo4j_DAO extends BaseNeo4jDAO implements ReviewDAO {
    @Override
    public Boolean updateReviewsByDeletedBaseUser(BaseUser user) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public boolean reviewMovie(String userId, String movieId, String content, Date date, Boolean freshness) throws DAOException{
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
}
