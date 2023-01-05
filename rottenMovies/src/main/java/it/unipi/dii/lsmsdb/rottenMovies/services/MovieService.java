package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.HallOfFameDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptionsEnum;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptionsEnum;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.MAXIMUM_NUMBER_OF_PERSONNEL;

public class MovieService {
    public PageDTO<MovieDTO> listMoviePage(int page, HashMap<String, String> request){
        PageDTO<MovieDTO> movie_page = new PageDTO<>();
        try(MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            moviedao.queryBuildSearchByTitle(request.getOrDefault("title", ""));
            for (Map.Entry<String, String> entry : request.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (v.isEmpty()){
                    continue;
                }
                if (k.equals("startYear") || k.equals("endYear")){
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
            System.err.println(e);
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
            System.err.println(e);
        }
        return movie;
    }

    public PageDTO<HallOfFameDTO> getHOFProductionHouses(String sort, int min_movie_count){
        PageDTO<HallOfFameDTO> hallOfFameDTO= new PageDTO<>();
        ArrayList<HallOfFameDTO> listHOF = new ArrayList<>();
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            switch (sort) {
                case "user":
                    listHOF = moviedao.mostSuccesfullProductionHouses(min_movie_count, new SortOptions(SortOptionsEnum.USER_RATING, -1));
                    break;
                default:
                    listHOF = moviedao.mostSuccesfullProductionHouses(min_movie_count, new SortOptions(SortOptionsEnum.TOP_CRITIC_RATING, -1));
                    break;
            }
            hallOfFameDTO.setEntries(listHOF);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return hallOfFameDTO;
    }
    public PageDTO<HallOfFameDTO> getHOFGenres(String sort, int min_movie_count){
        PageDTO<HallOfFameDTO> hallOfFameDTO= new PageDTO<>();
        ArrayList<HallOfFameDTO> listHOF = new ArrayList<>();
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            switch (sort) {
                case "user":
                    listHOF = moviedao.mostSuccesfullGenres(min_movie_count, new SortOptions(SortOptionsEnum.USER_RATING, -1));
                    break;
                default:
                    listHOF = moviedao.mostSuccesfullGenres(min_movie_count, new SortOptions(SortOptionsEnum.TOP_CRITIC_RATING, -1));
                    break;
            }
            hallOfFameDTO.setEntries(listHOF);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return hallOfFameDTO;
    }
    public PageDTO<HallOfFameDTO> getHOFYears(String sort, int min_movie_count) {
        PageDTO<HallOfFameDTO> hallOfFameDTO = new PageDTO<>();
        ArrayList<HallOfFameDTO> listHOF = new ArrayList<>();
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            switch (sort) {
                case "user":
                    listHOF = moviedao.bestYearsBasedOnRatings(min_movie_count, new SortOptions(SortOptionsEnum.USER_RATING, -1));
                    break;
                default:
                    listHOF = moviedao.bestYearsBasedOnRatings(min_movie_count, new SortOptions(SortOptionsEnum.TOP_CRITIC_RATING, -1));
                    break;
            }
            hallOfFameDTO.setEntries(listHOF);
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
        return hallOfFameDTO;
    }
//    <label for="title">title</label>
//  <input class="form-control" id="title" type="text" placeholder="Title" name="title" th:value="${movie.primaryTitle}?:''">
//
//  <label for="year">year</label>
//  <input class="form-control" id="year" type="number" placeholder="year" name="year" th:value="${movie.year}?:0">
//
//  <label for="runtimeMinutes">runtimeMinutes</label>
//  <input class="form-control" id="runtimeMinutes" type="number" placeholder="runtimeMinutes" name="runtimeMinutes" th:value="${movie.runtimeMinutes}?:0">
//
//  <label for="productionCompany">productionCompany</label>
//  <input class="form-control" id="productionCompany" type="text" placeholder="productionCompany" name="productionCompany" th:value="${movie.productionCompany}?:''">
//
//  <label for="genres">genres</label>
//  <input class="form-control" id="genres" type="text" placeholder="genres" name="title" th:value="${movie.genres}?:${#strings.listJoin(movie.genres,', ')}">
//
//<label>Personnel</label>
//<ul class="col-md-4 fs-6 mx-3" th:each="w:${movie.personnel}">
//  <label for="primaryName">primaryName</label>
//  <input class="form-control" id="primaryName" type="text" placeholder="primaryName" name="primaryName" th:value="${w.primaryName}?:''">
//  <label for="category">category</label>
//  <input class="form-control" id="category" type="text" placeholder="category" name="category" th:value="${w.category}?:''">
//  <label for="job_characters">job_characters</label>
//  <input class="form-control" id="job_characters" type="text" placeholder="job_characters" name="job_characters" th:value="${w.job}?:(${w.characters}?:'')">
//</ul>
//
//  <button class="w-10 mx-2 btn btn-lg btn-primary" type="submit" name="admin_operation" value="update">Update</button>
//  <button class="w-10 mx-2 btn btn-lg btn-danger" type="submit" name="admin_operation" value="delete">Delete</button>
    private Movie buildMovieFromForm(String mid, HashMap<String, String> hm){
        Movie newMovie = new Movie();
        newMovie.setId(new ObjectId(mid));
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v.isEmpty()) {
                continue;
            }
            if (k.equals("productionCompany")){
                newMovie.setProductionCompany(v);
            } else if (k.equals("year")){
                newMovie.setYear(Integer.parseInt(v));
            } else if (k.equals("runtimeMinutes")){
                newMovie.setRuntimeMinutes("");
            } else if (k.equals("genres")){
                newMovie.setGenres(Arrays.stream(v.split(",")).map(x -> {return x.trim();}).collect(Collectors.toCollection(ArrayList<String>::new))); // "Action, Adventure" -> ["Action", "Adventure"]
            }
        }
        String key, category, job_characters;
        ArrayList<Personnel> workers;
        for (int i = 0; i < MAXIMUM_NUMBER_OF_PERSONNEL; i++) {
            key = String.format("primaryName_%d", i);
            if (!hm.containsKey(key)){
                break;
            }
            Personnel worker = new Personnel();
            worker.setPrimaryName(hm.getOrDefault(key, ""));
            key = String.format("category_%d", i);
            category = hm.getOrDefault(key, "");
            worker.setCategory(category);
            if (category.equals("actor") || category.equals("actress")) {
                //worker.setPrimaryName(hm.getOrDefault(key, ""));
                job_characters = hm.getOrDefault(String.format("job_characters_%d", i), ""); //characters
            } else {
                job_characters = hm.getOrDefault(String.format("job_characters_%d", i), ""); //job
            }
            worker.setJob(job_characters);


        }
        //newMovie.setpersonnel();
        return newMovie;
    }

    public boolean modifyMovie(String mid, HashMap<String, String> hm) {
        String op = hm.get("admin_operation");
        Movie movie = buildMovieFromForm(mid, hm);
        return false;
        /*boolean result = false;
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)){
            if (op.equals("update")){
                result = moviedao.update(movie);
            } else if (op.equals("delete")){
                result = moviedao.delete(movie);
            }
        } catch (Exception e){
            System.err.println(e);
            return false;
        }
        if (result){
            try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j)){
                if (op.equals("update")){
                    result = moviedao.update(movie);
                } else if (op.equals("delete")){
                    result = moviedao.delete(movie);
                }
            } catch (Exception e){
                System.err.println(e);
                return false;
            }
            if (!result) { // mongo roll-back (insert)
                try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
                    result = moviedao.insert(movie);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }
            return true;
        }
        return false;*/
    }
}
