package it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserLeaderboardDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import org.bson.types.ObjectId;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.TransactionWork;

import java.util.ArrayList;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.NEO4J_DATABASE_STRING;

/**
 * <class>AdminNeo4j_DAO</class> is responsible for the admin-field operation in
 * the Neo4j
 */
public class AdminNeo4j_DAO extends BaseNeo4jDAO implements AdminDAO {
    /**
     * <method>getMostReviewUser</method> queries the graphdb to get most active user
     * based on how many reviews they have written
     * @return an ArrayList of UserLeaderboardDTO
     * @throws DAOException
     */
    @Override
    public ArrayList<UserLeaderboardDTO> getMostReviewUser() throws DAOException {
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        ArrayList<UserLeaderboardDTO> userList = session.readTransaction((TransactionWork<ArrayList<UserLeaderboardDTO>>) tx ->{
            String query = "MATCH (b)-[:REVIEWED]->(m:Movie) " +
                    "RETURN b.id as Id, b.name AS Name, count(*) as NumMovies, labels(b) as Type " +
                    "ORDER BY NumMovies DESC " +
                    "LIMIT 10";
            Result result = tx.run(query);
            ArrayList<UserLeaderboardDTO> list = new ArrayList<>();
            while(result.hasNext()){
                Record r = result.next();
                UserLeaderboardDTO user = new UserLeaderboardDTO();
                user.setId(new ObjectId(r.get("Id").asString()));
                user.setUsername(r.get("Name").asString());
                user.setCounter(r.get("NumMovies").asInt());
                user.setType(r.get("Type").get(0).asString());
                list.add(user);
            }
            return list;
        });
        return userList;
    }

    /**
     * <method>getMostFollowedCritic</method> queries the graph DB to get the most followed top critic
     * @return an ArrayList of UserLeaderboardDTO
     * @throws DAOException
     */
    @Override
    public ArrayList<UserLeaderboardDTO> getMostFollowedCritic() throws DAOException{
        Session session = driver.session(SessionConfig.forDatabase(NEO4J_DATABASE_STRING));
        ArrayList<UserLeaderboardDTO> userList = session.readTransaction((TransactionWork<ArrayList<UserLeaderboardDTO>>) tx ->{
            String query = "MATCH (t:TopCritic)<-[:FOLLOWS]-(u:User) " +
                    "RETURN t.id as Id, t.name AS Name, count(*) as NumMovies " +
                    "ORDER BY NumMovies DESC " +
                    "LIMIT 10";
            Result result = tx.run(query);
            ArrayList<UserLeaderboardDTO> list = new ArrayList<>();
            while(result.hasNext()){
                Record r = result.next();
                UserLeaderboardDTO user = new UserLeaderboardDTO();
                user.setId(new ObjectId(r.get("Id").asString()));
                user.setUsername(r.get("Name").asString());
                user.setCounter(r.get("NumMovies").asInt());
                list.add(user);
            }
            return list;


        });
        return userList;
    }
    @Override
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration(int offset) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration() throws DAOException{
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }

    @Override
    public boolean changeUserStatus(ObjectId userId, boolean ban) throws DAOException {
        throw new DAOException("requested a query for the MongoDB in the Neo4j connection");
    }
}
