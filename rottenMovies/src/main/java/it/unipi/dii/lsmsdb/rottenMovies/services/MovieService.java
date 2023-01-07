package it.unipi.dii.lsmsdb.rottenMovies.services;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.ReviewProjectionOptionsEnum;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptions;
import it.unipi.dii.lsmsdb.rottenMovies.utils.SortOptionsEnum;
import org.bson.types.ObjectId;

import java.util.*;
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
    private Movie buildMovieFromForm(String mid, HashMap<String, String> hm){
        Movie newMovie = new Movie();
        newMovie.setId(new ObjectId(mid));
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v==null || v.isEmpty()) {
                continue;
            }
            if (k.equals("title")){
                newMovie.setPrimaryTitle(v);
            } else if (k.equals("productionCompany")){
                newMovie.setProductionCompany(v);
            } else if (k.equals("year")){
                newMovie.setYear(Integer.parseInt(v));
            } else if (k.equals("runtimeMinutes")){
                newMovie.setRuntimeMinutes(Integer.parseInt(v));
            } else if (k.equals("genres")){
                newMovie.setGenres(
                        Arrays.stream(
                                v.split(","))
                                .map(
                                        x -> {
                                            return x.trim();
                                        })
                                .collect(
                                        Collectors.toCollection(
                                                ArrayList<String>::new
                                        )
                                )
                ); // "Action, Adventure" -> ["Action", "Adventure"]
            }
        }
        String key, category;
        ArrayList<Personnel> workers = new ArrayList<Personnel>();
        for (int i = 1; i < MAXIMUM_NUMBER_OF_PERSONNEL; i++) {
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
                worker.setCharacters(Arrays.stream(
                                hm.getOrDefault(
                                        String.format("job_characters_%d", i),
                                        "")
                                        .split(","))
                                .map(
                                        x -> {
                                            return x.trim();
                                        })
                                .collect(
                                        Collectors.toCollection(
                                                ArrayList<String>::new
                                        )));
            } else {
                worker.setJob(hm.getOrDefault(String.format("job_characters_%d", i), ""));
            }

            workers.add(worker);
        }
        newMovie.setpersonnel(workers);
        return newMovie;
    }

    public boolean modifyMovie(String mid, HashMap<String, String> hm) {
        String op = hm.get("admin_operation");
        Movie movie = buildMovieFromForm(mid, hm);
        boolean result = false;
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
                    moviedao.insert(movie);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public ObjectId addMovie(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        Movie newMovie = new Movie();
        newMovie.setPrimaryTitle(title);
        ObjectId id = null;
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
            id = moviedao.insert(newMovie);
        } catch (Exception e) {
            System.err.println(e);
        }
        if (id == null) {
            return null;
        }
        newMovie.setId(id);
        try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j)) {
            id = moviedao.insert(newMovie);
        } catch (Exception e) {
            System.err.println(e);
        }
        if (id == null){ // roll back di mongo
            try (MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)) {
                moviedao.delete(newMovie);
            } catch (Exception e) {
                System.err.println(e);
            }
            return null;
        }
        return id;
    }

// {score=Review Score: �A�,
// critic_operation=update,
// content=Overcomes its artificial contrivances to become a touching psychological drama about despair and loneliness.,
// isFresh=on}
    private Review buildReviewFromForm(String mid, HashMap<String, String> hm, RegisteredUserDTO credentials) {
        Review newReview = new Review();
        newReview.setMovie_id(new ObjectId(mid));
        newReview.setCriticName(credentials.getUsername());
        newReview.setReviewType("Rotten");
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v == null || v.isEmpty()) {
                continue;
            }
            if (k.equals("score")) {
                newReview.setReviewScore(v);
            } else if (k.equals("content")) {
                newReview.setReviewContent(v);
            } else if (k.equals("isFresh")) {
                newReview.setReviewType(v.equals("on") ? "Fresh" : "Rotten");
            } else if (k.equals("title")){
                newReview.setMovie(v);
            }
        }
        newReview.setTopCritic(credentials instanceof TopCriticDTO);
        newReview.setReviewDate_date(new Date());
        return newReview;
    }
    public boolean modifyReview(String mid, HashMap<String, String> hm, RegisteredUserDTO credentials) {
        String op = hm.get("critic_operation");
        Review review = buildReviewFromForm(mid, hm, credentials);
        boolean result = false;
        BaseUser user = (credentials instanceof TopCriticDTO) ?
                new TopCritic((TopCriticDTO) credentials) :
                new User((UserDTO) credentials);

        try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.MONGO)) {
            if (op.equals("update")) {
                result = reviewdao.update(user, review);
            } else if (op.equals("delete")) {
                result = reviewdao.delete(review);
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        if (result) {
            try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j)) {
                if (op.equals("update")) {
                    result = reviewdao.update(user, review);
                } else if (op.equals("delete")) {
                    result = reviewdao.delete(review);
                }
            } catch (Exception e) {
                System.err.println(e);
                return false;
            }
            if (!result) { // mongo roll-back (insert)
                try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.MONGO)) {
                    reviewdao.reviewMovie(user, review);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }
            return true;
        }
        return false;
    }
}
