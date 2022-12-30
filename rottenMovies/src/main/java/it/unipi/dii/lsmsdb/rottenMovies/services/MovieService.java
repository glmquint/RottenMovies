package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.utils.sortOptions;

import java.util.HashMap;
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
                } else if (k.equals("workers")){
                    String[] workers = v.split(",");
                    for (String w: workers){
                        System.out.println("Worker: " + w.trim());
                    }
                    moviedao.queryBuildSearchPersonnel(
                            v.split(","),
                            request.containsKey("includeAllWorkers") && request.get("includeAllWorkers").equals("on")
                    );
                } else if (k.equals("genres")){
                    String[] genres = v.split(",");
                    for (String w: genres){
                        System.out.println("Genres: " + w.trim());
                    }
                    moviedao.queryBuildSearchGenres(
                            v.split(","),
                            request.containsKey("includeAllGenres") && request.get("includeAllGenres").equals("on")
                    );
                }
            }
            sortOptions sort_opt = null;
            switch (request.getOrDefault("sort", "top_critic_rating")){
                case "date":
                    sort_opt = sortOptions.DATE;
                    break;
                case "alphabet":
                    sort_opt = sortOptions.ALPHABET;
                    break;
                case "user_rating":
                    sort_opt = sortOptions.USER_RATING;
                    break;
                case "top_critic_rating":
                default:
                    sort_opt = sortOptions.TOP_CRITIC_RATING;
            }
            System.out.println("searching with sort_opt: " + sort_opt);
            movie_page.setEntries(moviedao.executeSearchQuery(page, sort_opt, request.getOrDefault("sortOrder", "off").equals("off")?-1:1));
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return movie_page;
    }
}
