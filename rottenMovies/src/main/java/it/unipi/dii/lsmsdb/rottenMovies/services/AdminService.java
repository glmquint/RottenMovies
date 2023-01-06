package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.HashMap;

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
    public PageDTO<RegisteredUserDTO> listUserPage(int page, HashMap<String, String> request){
        PageDTO<RegisteredUserDTO> user_page = new PageDTO<>();
        try(BaseUserDAO baseUserDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            baseUserDAO.queryBuildSearchByUsername(request.getOrDefault("searchUser", ""));
            user_page.setEntries(baseUserDAO.executeSearchQuery(page));
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return user_page;
    }

    public boolean setBannedStatus(String userId, boolean toBan){
        try(AdminDAO adminDAO = DAOLocator.getAdminDAO(DataRepositoryEnum.MONGO)){
            return adminDAO.changeUserStatus(new ObjectId(userId), toBan);
        }
        catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
    }
    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month){
        try(ReviewDAO reviewDAO = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j)){
            return reviewDAO.checkReviewBombing(movie,month);
        }
        catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

}
