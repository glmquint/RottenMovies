package it.unipi.dii.lsmsdb.rottenMovies.DAO;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseNeo4jDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.BaseUserMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.MovieMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j.BaseUserNeo4j_DAO;

public class DAOLocator {
    public static MovieDAO getMovieDAO(){
        return new MovieMongoDB_DAO();
    }
    public static BaseUserDAO getBaseUserDAO(){
        return new BaseUserMongoDB_DAO();
    }

    public static BaseUserDAO getBaseUserDAO_neo4j(){return new BaseUserNeo4j_DAO();}
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
