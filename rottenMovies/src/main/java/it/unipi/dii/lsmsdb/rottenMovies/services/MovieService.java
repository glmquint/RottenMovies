package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptionsEnum;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptionsEnum;
import org.bson.types.ObjectId;

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
            //moviedao.queryBuildSearchByTitle("");
            for (Map.Entry<String, String> entry : request.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (v.isEmpty()){
                    continue;
                }
                if (k.equals("title")) {
                    moviedao.queryBuildSearchByTitle(v);
                } else if (k.equals("startYear") || k.equals("endYear")){
                    moviedao.queryBuildSearchByYear(Integer.parseInt(v), k.equals("startYear"));
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
            SortOptionsEnum sort_opt = null;
            switch (request.getOrDefault("sort", "top_critic_rating")){
                case "date":
                    sort_opt = SortOptionsEnum.DATE;
                    break;
                case "alphabet":
                    sort_opt = SortOptionsEnum.ALPHABET;
                    break;
                case "user_rating":
                    sort_opt = SortOptionsEnum.USER_RATING;
                    break;
                case "top_critic_rating":
                default:
                    sort_opt = SortOptionsEnum.TOP_CRITIC_RATING;
            }
            System.out.println("searching with sort_opt: " + sort_opt);
            movie_page.setEntries(moviedao.executeSearchQuery(page,
                    new SortOptions(sort_opt, request.getOrDefault("sortOrder", "off").equals("off")?-1:1),
                    new ReviewProjectionOptions(ReviewProjectionOptionsEnum.HIDE, 0)));
        } catch (Exception e){
            System.err.println(e.getStackTrace());
        }
        return movie_page;
    }

    public MovieDTO getMovie(int page, String movie_id, int comment_index) {
        MovieDTO movie = new MovieDTO();
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            moviedao.queryBuildSearchById(new ObjectId(movie_id));
            if (comment_index <0) {
                movie = moviedao.executeSearchQuery(0,
                        new SortOptions(SortOptionsEnum.NO_SORT, -1),
                        new ReviewProjectionOptions(ReviewProjectionOptionsEnum.SLICE, page)).get(0);
            } else{
                movie = moviedao.executeSearchQuery(0,
                        new SortOptions(SortOptionsEnum.NO_SORT, -1),
                        new ReviewProjectionOptions(ReviewProjectionOptionsEnum.SINGLE, comment_index)).get(0);
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return movie;
    }
}
