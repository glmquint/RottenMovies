package it.unipi.dii.lsmsdb.rottenMovies.backend.mongoDB;

import it.unipi.dii.lsmsdb.rottenMovies.backend.base.MongoDBConnector;
import it.unipi.dii.lsmsdb.rottenMovies.backend.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

public class MovieMongoDB_DAO extends MongoDBConnector implements MovieDAO {

    public Movie searchByTitle(String title){}
    public Movie searchByYear(int year){}
    public Movie searchByTopRatings(int rating){}
    public Movie searchByUserRatings(int rating){}
}
