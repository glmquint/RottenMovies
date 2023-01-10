package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserLeaderboardDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * <interface>AdminDao</interface> is the base interface that offers the methods below
 *  to the AdminMongoDB_DAO and AdminNeo4j_DAO classes that implement them
 */
public interface AdminDAO extends AutoCloseable{
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration (int offset) throws DAOException;
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration () throws DAOException;
    boolean changeUserStatus(ObjectId userId,boolean ban) throws DAOException;

    public ArrayList<UserLeaderboardDTO> getMostReviewUser() throws DAOException;
    public ArrayList<UserLeaderboardDTO> getMostFollowedCritic() throws DAOException;
}
