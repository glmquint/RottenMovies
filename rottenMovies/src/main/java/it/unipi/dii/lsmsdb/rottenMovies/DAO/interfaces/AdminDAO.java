package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;

import java.util.ArrayList;

public interface AdminDAO extends AutoCloseable{
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration (int start, int offset, int index);
    ArrayList<PopulationByGenerationDTO> userPopulationByGeneration ();
}
