package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieReviewBombingDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * <class>AdminService</class> contains all the utility method for the admin user
 */
public class AdminService {

    /**
     * <method>getPopulationByGeneration</method> allows the creation of a bucket list that divides the
     * user population in base of their date of birth
     * @param offset is the dimension of the bucket
     * @return a PageDTO of PopulationByGenerationDTO for visualization
     */
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

    /**
     * <method>listUserPage</method> allows the admin to search for user in the DB
     * @param page is the page use for pagination
     * @param request contains the information for which user to search for
     * @return a PageDTO of RegisteredUserDTO for visualization
     */
    public PageDTO<RegisteredUserDTO> listUserPage(int page, HashMap<String, String> request){
        PageDTO<RegisteredUserDTO> user_page = new PageDTO<>();
        try(BaseUserDAO baseUserDAO = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)) {
            baseUserDAO.queryBuildSearchByUsername(request.getOrDefault("searchUser", ""));
            baseUserDAO.queryBuildExcludeAdmin();
            user_page.setEntries(baseUserDAO.executeSearchQuery(page));
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return user_page;
    }

    /**
     * <method>setBannedStatus</method> allows an admin to ban/unban a user from the service
     * @param userId is the id of the user to ban/unban
     * @param toBan represent if the operation is a ban one (true) or an unban one (false)
     * @return true if the transaction completed successfully
     */
    public boolean setBannedStatus(String userId, boolean toBan){
        try(AdminDAO adminDAO = DAOLocator.getAdminDAO(DataRepositoryEnum.MONGO)){
            return adminDAO.changeUserStatus(new ObjectId(userId), toBan);
        }
        catch (Exception e){
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    /**
     * <method>checkReviewBombing</method> allows an admin to check if a movie has been target by review bombing
     * @param movie is the model of the movie the admin wants to check
     * @param month is the number of month from today to use as delimiter
     * @return MovieReviewBombingDTO for visualization
     */
    public MovieReviewBombingDTO checkReviewBombing(Movie movie, int month){
        try(ReviewDAO reviewDAO = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j)){
            return reviewDAO.checkReviewBombing(movie,month);
        }
        catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    /**
     * <method>getMostReviewUser</method> allows the admin to check for the most active User in terms of
     * number of movie reviewed
     * @return an ArrayList of UserLeaderboardDTO for visualization
     */
    public ArrayList<UserLeaderboardDTO> getMostReviewUser(){
        ArrayList<UserLeaderboardDTO> list = new ArrayList<>();
        try(AdminDAO adminDAO = DAOLocator.getAdminDAO(DataRepositoryEnum.NEO4j)){
            list = adminDAO.getMostReviewUser();
        }catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return list;
    }

    /**
     * <method>getFollowTopCritic</method> allows the admin to check for the most followed TopCritic
     * @return an ArrayList of UserLeaderboardDTO
     */
    public ArrayList<UserLeaderboardDTO> getFollowTopCritic() {
        ArrayList<UserLeaderboardDTO> list = new ArrayList<>();
        try(AdminDAO adminDAO = DAOLocator.getAdminDAO(DataRepositoryEnum.NEO4j)){
            list = adminDAO.getMostFollowedCritic();
        }catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return list;
    }


}
