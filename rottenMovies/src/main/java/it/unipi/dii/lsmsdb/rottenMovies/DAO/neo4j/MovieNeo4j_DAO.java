package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.HallOfFameDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.YearMonthReviewDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import org.bson.types.ObjectId;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.ArrayList;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.NEO4J_DATABASE_STRING;
import static org.neo4j.driver.Values.parameters;

/**

 * <class>MovieNeo4j_DAO</class> allow to use methods to interact with the GraphDB specifically for the movie entities
 */
public class MovieNeo4j_DAO extends BaseNeo4jDAO implements MovieDAO {
    /**
     * <method>insert</method> add a new entity to the GraphDB
     *
     * @param movie is the model from which the new entity is generate
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public ObjectId insert(Movie movie) throws DAOException {
        String id = movie.getId().toString();
        String title = movie.getPrimaryTitle();
        if (id.isEmpty() || title.isEmpty()) {
            return null;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MERGE (m:Movie{id: $id}) " +
                    "ON CREATE SET m.id= $id, m.title = $title " +
                    "RETURN m.title as Title";
            Result result = tx.run(query, parameters("id", id, "title", title));
            System.out.println(result.single().get("Title").asString());
            return 1;
        });
        return movie.getId();
    }

    /**
     * <method>delete</method> remove an entity to the GraphDB
     * @param movie is the model used to get the info to retrieve and delete from the GraphDB
     * @return true in case of success
     * @throws DAOException
     */
    @Override
    public boolean delete(Movie movie) throws DAOException, NoSuchRecordException {
        String id = movie.getId().toString();
        if (id.isEmpty()) {
            return false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MATCH (m:Movie{id: $id}) " +
                    "DETACH DELETE m";
            Result result = tx.run(query, parameters("id", id));
            return 1;
        });
        return true;
    }

    /**
     * <mathod>update</mathod> updates the title of a movie,
     * in the actual state of the application this method is never called,
     * but it is kept in case of future change in development
     * @param movie is the model with the updated info
     * @return true if the transaction concluded correctly
     * @throws DAOException
     * @throws NoSuchRecordException
     */
    public boolean update(Movie movie) throws DAOException, NoSuchRecordException {
        String id = movie.getId().toString();
        String newTitle = movie.getPrimaryTitle();
        if (id.isEmpty() || newTitle.isEmpty()) {
            return false;
        }
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        session.writeTransaction(tx -> {
            String query = "MATCH (m:Movie{id: $id}) " +
                    "SET m.title = $newTitle RETURN m.title AS Title";
            Result result = tx.run(query, parameters("id", id, "newTitle", newTitle));
            System.out.println(result.single().get("Title").asString());
            return 1;
        });
        return true;
    }


    public ArrayList<MovieDTO> executeSearchQuery(int page) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public ArrayList<MovieDTO> executeSearchQuery(int page, SortOptions sort_opt, ReviewProjectionOptions proj_opt) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public boolean executeDeleteQuery() throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }


    public void queryBuildSearchByTitleContains(String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByTitleExact (String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByTitle (String title) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchById (ObjectId id) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchByTopRatings ( int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildsearchByUserRatings ( int rating, boolean type) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    @Override
    public void queryBuildSearchPersonnel (String[]workers,boolean includeAll) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public void queryBuildSearchGenres (String[]genres,boolean includeAll) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public void queryBuildSearchByYear ( int year, boolean afterYear) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public ArrayList<HallOfFameDTO> mostSuccesfullProductionHouses(int numberOfMovies, SortOptions opt) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");

    }
    public ArrayList<HallOfFameDTO> mostSuccesfullGenres(int numberOfMovies, SortOptions opt) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
    public ArrayList<HallOfFameDTO> bestYearsBasedOnRatings (int numberOfMovies, SortOptions opt) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    public ArrayList<YearMonthReviewDTO> getYearAndMonthReviewActivity(ObjectId id) throws DAOException{
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }


}
