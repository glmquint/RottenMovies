package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieService {
    private MovieDAO movieMongoDAO;
    private MovieDAO movieNeo4jDAO;
    public MovieService(){
        this.movieMongoDAO = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO);
        this.movieNeo4jDAO = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j);
    }

    public PageDTO<MovieDTO> listMoviePage(int page, HashMap<String, String> request){
        PageDTO<MovieDTO> movie_page = new PageDTO<>();
        try(MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            moviedao.queryBuildSearchByTitle("");
            for (Map.Entry<String, String> entry : request.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (k.equals("title")) {
                    moviedao.queryBuildSearchByTitle(v);
                } else if (k.equals("startYear") || k.equals("endYear")){
                    if (!v.isEmpty()) {
                        moviedao.queryBuildSearchByYear(Integer.parseInt(v), k.equals("startYear"));
                    }
                }
            }
            movie_page.setEntries(moviedao.executeSearchQuery(page));
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return movie_page;
    }
}
