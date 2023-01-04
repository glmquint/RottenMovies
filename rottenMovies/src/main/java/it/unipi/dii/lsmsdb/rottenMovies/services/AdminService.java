package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;


import java.util.ArrayList;

public class AdminService {
    public PageDTO<PopulationByGenerationDTO> getPopulationByGeneration (int offset){
        PageDTO<PopulationByGenerationDTO> population= new PageDTO<>();
        ArrayList<PopulationByGenerationDTO> listPopulation = new ArrayList<>();
        try (AdminDAO admindao = DAOLocator.getAdminDAO(DataRepositoryEnum.MONGO)) {
            listPopulation=admindao.userPopulationByGeneration(offset);
            population.setEntries(listPopulation);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return population;
    }
}
