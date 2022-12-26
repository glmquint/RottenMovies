package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;

import java.util.List;

public class MovieService {
    private MovieDAO movieMongoDAO;
    private MovieDAO movieNeo4jDAO;
    public MovieService(){
        this.movieMongoDAO = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO);
        this.movieNeo4jDAO = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j);
    }

    //public PageDTO<MovieDTO> listMoviePage(int page, String searchKeyword, List<String> searchGenres, int startYear, int endYear, )
}
