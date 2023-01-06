package it.unipi.dii.lsmsdb.rottenMovies.DAO;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.AdminMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.BaseUserMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.MovieMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.ReviewMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j.AdminNeo4j_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j.BaseUserNeo4j_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j.MovieNeo4j_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.neo4j.ReviewNeo4j_DAO;

public class DAOLocator {
    public static MovieDAO getMovieDAO(DataRepositoryEnum dataRepositoryEnum){
        if (DataRepositoryEnum.MONGO.equals(dataRepositoryEnum)) {
            return new MovieMongoDB_DAO();
        } else if (DataRepositoryEnum.NEO4j.equals(dataRepositoryEnum)) {
            return new MovieNeo4j_DAO();
        }
        throw new UnsupportedOperationException("Data repository not supported: " + dataRepositoryEnum);
    }
    public static BaseUserDAO getBaseUserDAO(DataRepositoryEnum dataRepositoryEnum){
        if (DataRepositoryEnum.MONGO.equals(dataRepositoryEnum)) {
            return new BaseUserMongoDB_DAO();
        } else if (DataRepositoryEnum.NEO4j.equals(dataRepositoryEnum)) {
            return new BaseUserNeo4j_DAO();
        }
        throw new UnsupportedOperationException("Data repository not supported: " + dataRepositoryEnum);
    }
    public static AdminDAO getAdminDAO(DataRepositoryEnum dataRepositoryEnum){
        if (DataRepositoryEnum.MONGO.equals(dataRepositoryEnum)) {
            return new AdminMongoDB_DAO();
        }
        else if (DataRepositoryEnum.NEO4j.equals(dataRepositoryEnum)) {
            return new AdminNeo4j_DAO();
        }
        throw new UnsupportedOperationException("Data repository not supported: " + dataRepositoryEnum);
    }
    public static ReviewDAO getReviewDAO(DataRepositoryEnum dataRepositoryEnum){
        if (DataRepositoryEnum.MONGO.equals(dataRepositoryEnum)) {
            return new ReviewMongoDB_DAO();
        } else if (DataRepositoryEnum.NEO4j.equals(dataRepositoryEnum)) {
            return new ReviewNeo4j_DAO();
        }
        throw new UnsupportedOperationException("Data repository not supported: " + dataRepositoryEnum);
    }
}
