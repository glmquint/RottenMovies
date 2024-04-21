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

/**
 * <class>MovieService</class> contains all the utility method to pass the modification
 * from front-end to back-end regarding the movie entities and its reviews
 */
public class MovieService {
    /**
     * <method>listMoviePage</method> allows to build pages in the explorer where the movies,
     * according to the filters, are shown
     * @param page is the page use for pagination
     * @param request contains the filters for which movie to search for
     * @return a PageDTO of MovieDTO for visualization
     */
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

    /**
     * <method>getMovie</method> allows to get a movie from the back-end and transport it to the visualization layer
     * @param page is the page use for pagination
     * @param movie_id is the id of the target movie
     * @param comment_index is the index a specific review to visualize
     * @return a MovieDTO for visualization
     */
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

    /**
     * <method>getHOFProductionHouses</method> builds the HoF for the production houses after querying the back-end
     * @param sort is the type of sort requested
     * @param min_movie_count is the minimum movie produced count to be a part of the HoF
     * @return a PageDTO of HallOfFameDTO for visualization
     */
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

    /**
     * <method>getHOFGenres</method> builds the HoF for genres after querying the back-end
     * @param sort is the type of sort requested
     * @param min_movie_count is the minimum movie produced count to be a part of the HoF
     * @return a PageDTO of HallOfFameDTO for visualization
     */
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

    /**
     * <method>getHOFYears</method> builds the HoF for years after querying the back-end
     * @param sort is the type of sort requested
     * @param min_movie_count is the minimum movie produced count to be a part of the HoF
     * @return a PageDTO of HallOfFameDTO for visualization
     */
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

    /**
     * <method>buildMovieFromForm</method> creates a new Movie model from the data passed from the front-end
     * @param mid is the new id of the movie
     * @param hm contains all the information passed from the front-end
     * @return a Movie model
     */
    private Movie buildMovieFromForm(String mid, HashMap<String, String> hm){
        Movie newMovie = new Movie();
        newMovie.setId(new ObjectId(mid));
        newMovie.setPosterUrl("/images/poster_not_found.jpg");
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
            } else if (k.equals("runtimeMinutes")) {
                newMovie.setRuntimeMinutes(Integer.parseInt(v));
            } else if (k.equals("posterUrl")){
                newMovie.setPosterUrl(v);
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

    /**
     * <method>modifyMovie</method> allows the update of a movie already present in the db with data
     * passed from the front-end
     * @param mid is the movie id
     * @param hm contains the new data
     * @return true if the modification were completed successfully
     */
    public boolean modifyMovie(String mid, HashMap<String, String> hm) {
        String op = hm.get("admin_operation");
        Movie movie = buildMovieFromForm(mid, hm);
        System.out.println("new updated movie: " + movie);
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
        /* Don't need neo4j update if movie Title is invariant
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
         */
        return true;
    }

    /**
     * <method>addMovie</method> insert a new movie into the DBs with default values
     * @param title is the title of the new movie
     * @return the ObjectId of the newly inserted movie
     */
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


    /**
     * <method>buildReviewFromForm</method> builds a new review with data passed from the front-end
     * @param mid is the id of the reviewed movie
     * @param hm contains the data of the new review
     * @param credentials contains the information on the author of the review
     * @return a Review model
     */
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

    /**
     * <method>modifyReview</method> allows the modification of a review with data passed from the front-end
     * @param mid is the id of the reviewed movie
     * @param hm contains the information for completing the modification
     * @param credentials is the author of the review
     * @return true if the modification were applied successfully
     */
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

    /**
     * <method>reviewMovie</method> allows the creation of a new review in the DB with data passed from the front-end
     * @param credentials contains information on the author of the review
     * @param movieId is the id of the reviewed movie
     * @param movieTitle is the title of the reviewed movie
     * @return true if the insert of the review was completed successfully
     */
    public boolean reviewMovie(RegisteredUserDTO credentials, String movieId, String movieTitle) {
        BaseUser user = (credentials instanceof TopCriticDTO) ?
                new TopCritic((TopCriticDTO) credentials) :
                new User((UserDTO) credentials);
        Review review = new Review();
        review.setMovie_id(new ObjectId(movieId));
        review.setCriticName(credentials.getUsername());
        review.setMovie(movieTitle);
        review.setReviewDate_date(new Date());
        review.setReviewType("Fresh");
        review.setReviewScore("");
        review.setTopCritic(user instanceof TopCritic);
        review.setReviewContent("");
        boolean result = false;
        try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.MONGO)) {
            result = reviewdao.reviewMovie(user, review);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (result) {
            try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j)) {
                result = reviewdao.reviewMovie(user, review);
            } catch (Exception e) {
                System.err.println(e);
                return false;
            }
            if (!result) { // mongo roll-back (insert)
                try (ReviewDAO reviewdao = DAOLocator.getReviewDAO(DataRepositoryEnum.MONGO)) {
                    reviewdao.delete(review);
                } catch (Exception e) {
                    System.err.println(e);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public ArrayList<YearMonthReviewDTO> getMovieStat(String mid) {
        ArrayList<YearMonthReviewDTO> yearMonthReviewDTOS = new ArrayList<YearMonthReviewDTO>();
        try(MovieDAO movieDAO = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)){
            yearMonthReviewDTOS = movieDAO.getYearAndMonthReviewActivity(new ObjectId(mid));
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return yearMonthReviewDTOS;
    }
}
