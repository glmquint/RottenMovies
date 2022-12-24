package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.types.ObjectId;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.List;

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
    @Override
    public MovieDTO searchByTitle(String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public MovieDTO searchById(ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public List<MovieDTO> searchByYearRange(int startYear, int endYear) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public List<MovieDTO> searchByTopRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public List<MovieDTO> searchByUserRatings(int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public Boolean delete(MovieDTO toDelete) throws DAOException {
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
}
