package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface AdminDAO extends AutoCloseable{
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration (int offset);
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration ();
    boolean changeUserStatus(ObjectId userId,boolean ban);
}
