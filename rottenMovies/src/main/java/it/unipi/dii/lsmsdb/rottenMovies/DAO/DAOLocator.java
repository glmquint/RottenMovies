package it.unipi.dii.lsmsdb.rottenMovies.DAO;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.BaseUserMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.MovieMongoDB_DAO;

public class DAOLocator {
    public static MovieDAO getMovieDAO(){
        return new MovieMongoDB_DAO();
    }
    public static BaseUserMongoDB_DAO getBaseUserDAO(){
        return new BaseUserMongoDB_DAO();
    }
    /*public static AdminDAO getAdminDAO(){
        return new MovieMongoDB_DAO();
    }
    public static PersonnelDAO getPersonnelDAO(){
        return new MovieMongoDB_DAO();
    }
    public static ReviewDAO getReviewDAO(){
        return new MovieMongoDB_DAO();
    }
    public static TopCriticDAO getTopCriticDAO(){
        return new MovieMongoDB_DAO();
    }*/
}
