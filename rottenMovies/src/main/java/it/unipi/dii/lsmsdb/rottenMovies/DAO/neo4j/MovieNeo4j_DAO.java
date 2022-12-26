package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class MovieNeo4j_DAO extends BaseNeo4jDAO implements MovieDAO {
    public Boolean insertNeo4j(String id, String title) throws DAOException{
        if(id.isEmpty() || title.isEmpty()){
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MERGE (m:Movie{id: $id}) "+
                    "ON CREATE SET m.id= $id, m.title = $title "+
                    "RETURN m.title as Title";
            Result result = tx.run(query, parameters("id", id, "title", title));
            System.out.println(result.single().get("Title").asString());
            return 1;
        });
        return true;
    }

    public Boolean deleteNeo4j(String id) throws DAOException, NoSuchRecordException{
        if(id.isEmpty()){
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (m:Movie{id: $id}) " +
                    "DETACH DELETE m";
            Result result = tx.run(query, parameters("id", id));
            return 1;
        });
        return true;
    }

    public Boolean updateNeo4j(String id, String newTitle) throws DAOException, NoSuchRecordException {
        if(id.isEmpty() || newTitle.isEmpty()){
            return false;
        }
        Session session = driver.session();
        session.writeTransaction(tx -> {
            String query = "MATCH (m:Movie{id: $id}) " +
                    "SET m.title = $newTitle RETURN m.title AS Title";
            Result result = tx.run(query, parameters("id", id, "newTitle", newTitle));
            System.out.println(result.single().get("Title").asString());
            return 1;
        });
        return true;
    }



    /*@Override
    public MovieDTO searchByTitle(String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public MovieDTO searchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<MovieDTO> searchByYearRange(int startYear, int endYear) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<MovieDTO> searchByTopRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<MovieDTO> searchByUserRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean delete(MovieDTO toDelete) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
     */
    public ArrayList<MovieDTO> executeSearchQuery(int page) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public Boolean executeDeleteQuery() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByTitle (String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByTitleContains(String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildSearchByTopRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public void queryBuildsearchByUserRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public void queryBuildSearchByYear(int year, boolean afterYear) throws DAOException{
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean update(MovieDTO updated) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean insert(MovieDTO newOne) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return null;
    }
}
