package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import com.mongodb.client.MongoCollection;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class MovieNeo4j_DAO extends BaseNeo4jDAO implements MovieDAO {
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
