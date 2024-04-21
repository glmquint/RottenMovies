package it.unipi.dii.lsmsdb.rottenMovies.controller;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import it.unipi.dii.lsmsdb.rottenMovies.services.AdminService;
import it.unipi.dii.lsmsdb.rottenMovies.services.MovieService;
import it.unipi.dii.lsmsdb.rottenMovies.services.UserService;
import it.unipi.dii.lsmsdb.rottenMovies.services.UserService;
import it.unipi.dii.lsmsdb.rottenMovies.utils.MD5;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import static java.lang.Math.max;

@SpringBootApplication
@Controller
public class AppController {

    @GetMapping("/hello/{id}")
    public String hello(Model model,
                        HttpServletRequest request,
                        @RequestParam(value = "name", defaultValue = "World") String name,
                        @RequestParam(value = "amount", defaultValue = "1") String amount,
                        @PathVariable(value = "id") int id) {
        //model.addAttribute("return", String.format("Hello %s!", name));
        HashMap<String, String> hm = extractRequest(request);
        model.addAttribute("return", String.format("Hello %d", id));
        model.addAllAttributes(hm);
		/*List<Movie> movies = new
		for (int i = 0; i < amount; i++) {

		}*/
        return "hello";
    }

    private static HashMap<String, String> extractRequest(HttpServletRequest request) {
        Enumeration enumeration = request.getParameterNames();
        HashMap<String, String> modelMap = new HashMap<>();
        while(enumeration.hasMoreElements()){
            String parameterName = enumeration.nextElement().toString();
            modelMap.put(parameterName, request.getParameter(parameterName));
        }
        return modelMap;
    }

    @GetMapping("/")
    public String index(Model model,
                        HttpSession session){
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        model.addAttribute("credentials", credentials);
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model, HttpSession session, HttpServletRequest request){
        //session.setAttribute("credentials", session.getAttribute("credentials"));
        System.out.println("credentials: " + session.getAttribute("credentials"));
        if (session.getAttribute("credentials")!=null) {
            model.addAttribute("info", "you're already logged in");
            return "login";
        }
        UserService userService = new UserService();
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        RegisteredUserDTO registeredUserDTO = null;
        if (!hm.containsKey("username") || !hm.containsKey("password")) {
            return "login";
        }
        registeredUserDTO = userService.authenticate(hm.get("username"), MD5.getMd5(hm.get("password")));
        if (registeredUserDTO == null) {
            //hm.put("error", "invalid username or password");
            model.addAttribute("error", "invalid username or password");
            return "login";
        }
        model.addAttribute("credentials", registeredUserDTO);
        session.setAttribute("credentials", registeredUserDTO);
        System.out.println(registeredUserDTO.getClass());
        model.addAttribute("success", "login successful");
        return "login";
    }

    @PostMapping("/register")
    public String register(Model model, HttpSession session, HttpServletRequest request){
        UserService userService = new UserService();
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        RegisteredUserDTO registeredUserDTO = userService.register(hm);
        if (registeredUserDTO == null){
            model.addAttribute("error", "something went wrong during registration");
            return "register";
        }
        registeredUserDTO = userService.authenticate(hm.get("username"), MD5.getMd5(hm.get("password")));
        session.setAttribute("credentials", registeredUserDTO);
        model.addAttribute("credentials", registeredUserDTO);
        return "register";
    }

    @GetMapping("/register")
    public String registerGet(Model model,
                              HttpSession session){
        if (session.getAttribute("credentials")!=null) {
            model.addAttribute("info", "you're already logged in");
        }
        return "register";
    }

    @PostMapping("/admin-panel/registerTopCritic")
    public String registerTopCritic(Model model, HttpSession session, HttpServletRequest request){
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        if (credentials == null || !(credentials instanceof AdminDTO)) {
            model.addAttribute("error", "this operation isn't permitted to non-admin users");
            return "index";
        }
        UserService userService = new UserService();
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        RegisteredUserDTO registeredUserDTO = userService.register(hm);
        if (registeredUserDTO == null){
            model.addAttribute("error", "something went wrong during registration");
            return "registerTopCritic";
        }
        model.addAttribute("success", "new Top Critic successfully created");
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "registerTopCritic";
    }

    @GetMapping("/admin-panel/registerTopCritic")
    public String registerTopCriticGet(Model model,
                              HttpSession session){
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        if (credentials == null || !(credentials instanceof AdminDTO)) {
            model.addAttribute("error", "this operation isn't permitted to non-admin users");
            return "index";
        }
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "registerTopCritic";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        session.invalidate();
        return "logout";
    }

    @GetMapping("/movie")
    public  String explore_movie(Model model,
                                 HttpServletRequest request,
                                 HttpSession session){
        //System.out.println("requested page");
        //System.out.println(page);
        //model.addAttribute("page", page);
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        model.addAttribute("credentials", session.getAttribute("credentials"));
        MovieService movieService = new MovieService();
        HashMap<String, String> hm = extractRequest(request);
        ObjectId id = null;
        if (hm.getOrDefault("addMovie", "").equals("addMovie")){
            id = movieService.addMovie(hm.getOrDefault("title", ""));
            if (id == null){
                model.addAttribute("error", "error while creating a new Movie");
            } else {
                model.addAttribute("redirect", "/movie/" + id);
                return "exploreMovies";
            }
        }
        int page = 0;
        if (hm.containsKey("page")){
            page = Integer.parseInt(hm.get("page"));
        } else if (hm.containsKey("newPage") && hm.containsKey("currentPage")){
            page = max(Integer.parseInt(hm.get("currentPage")) + ((hm.get("newPage").equals("Next"))?1:-1), 0);
            hm.put("page", String.valueOf(page));
        }
        model.addAllAttributes(hm);
        System.out.println(hm);
        PageDTO<MovieDTO> movieList = movieService.listMoviePage(page, hm);
        model.addAttribute("movieList", movieList.getEntries());
        return "exploreMovies";
    }

    @RequestMapping("/movie-stat/{mid}")
    public String get_movie_stat(Model model,
                                 @RequestParam(value = "title", defaultValue = "") String title,
                                 @PathVariable(value = "mid") String mid,
                                 HttpSession session){
        MovieService movieService = new MovieService();
        model.addAttribute("yearMonthReviews", movieService.getMovieStat(mid));
        model.addAttribute("months", new String[]{"January", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        model.addAttribute("title", title);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "movie-stat";
    }

    @RequestMapping("/movie/{mid}")
    public  String select_movie(Model model,
                                HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @PathVariable(value = "mid") String mid,
                                HttpSession session){
        System.out.println("credentials: " + session.getAttribute("credentials"));
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        MovieService movieService = new MovieService();
        UserService userService = new UserService();
        boolean result = false;
        if (page < 0){
            page = 0;
        }

        if (hm.containsKey("admin_operation")){
            if (credentials == null || !(credentials instanceof AdminDTO)) {
                model.addAttribute("error", "this operation isn't permitted to non-admin users");
                return "index";
            }
//          if(hm.getOrDefault("admin_operation", "").equals("reviewBombing")){
//              String urlPath = "/review-bombing/"+hm.getOrDefault("title", "");
//              model.addAttribute("redirect", urlPath);
//              return "movie";
//          }

            result = movieService.modifyMovie(mid, hm);
            if (result){
                model.addAttribute("success", "movie successfully updated");
            } else {
                model.addAttribute("error", "error while updating movie");
            }
        } else if (hm.containsKey("critic_operation")){
            result = movieService.modifyReview(mid, hm, credentials);
            if (result){
                model.addAttribute("success", "review successfully updated");
            } else {
                model.addAttribute("error", "error while updating review");
            }
        } else if (hm.containsKey("user_operation")) {
            ArrayList<Object> movieAndIndex = userService.getReviewIndex(credentials.getId(), hm.getOrDefault("title", ""));
            if (movieAndIndex == null || movieAndIndex.size() != 2) {
                movieService.reviewMovie(credentials, hm.getOrDefault("movieId", ""), hm.getOrDefault("title", ""));
                movieAndIndex = null;
            }
            if (movieAndIndex == null){
                movieAndIndex = userService.getReviewIndex(credentials.getId(), hm.getOrDefault("title", ""));
                if (movieAndIndex == null || movieAndIndex.size() != 2) {
                    model.addAttribute("error", "error while creating a new review");
                    movieAndIndex = null;
                }
            }
            if (movieAndIndex != null && movieAndIndex.size() == 2) {
                // refresh credentials

                session.setAttribute("credentials",
                        userService.authenticate(
                            ((RegisteredUserDTO) session.getAttribute("credentials")).getUsername(),
                            ((RegisteredUserDTO) session.getAttribute("credentials")).getPassword())
                );
                model.addAttribute("redirect", "/movie/" + movieAndIndex.get(0) + "/" + movieAndIndex.get(1));
                return "redirect";
            }
        } else if(hm.containsKey("view")){
            RegisteredUserDTO topCritic = userService.getUserByUsername(hm.get("view"));
            model.addAttribute("go_to_user", topCritic.getId().toString());
        }
        model.addAttribute("movie", movieService.getMovie(page, mid, -1));
        model.addAttribute("page", page);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "movie";
    }

    @RequestMapping("/movie/{mid}/{comment_index}")
    public  String select_movie_comment(Model model,
                                        HttpServletRequest request,
                                        @PathVariable(value = "mid") String mid,
                                        @PathVariable(value = "comment_index") int comment_index,
                                        HttpSession session){
        MovieService movieService = new MovieService();
        RegisteredUserDTO credentials = (RegisteredUserDTO) session.getAttribute("credentials");
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        boolean result = false;
        if (hm.containsKey("critic_operation")) {
            result = movieService.modifyReview(mid, hm, credentials);
            if (result) {
                model.addAttribute("success", "review successfully updated");
            } else {
                model.addAttribute("error", "error while updating review");
            }
        }
        model.addAttribute("credentials", session.getAttribute("credentials"));
        model.addAttribute("movie", movieService.getMovie(0, mid, comment_index));
        return "movie";
    }

    @RequestMapping("/user/{uid}")
    public  String select_user(Model model,
                                HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @PathVariable(value = "uid") String uid,
                                HttpSession session){
        UserService userService = new UserService();
        if (page < 0){
            page = 0;
        }
        HashMap<String, String> hm = extractRequest(request);
        if(hm.containsKey("user_operation")){
            boolean result;
            if(hm.containsKey("birthday"))
                result = userService.modifyUser(uid, hm, false);
            else
                result = userService.modifyUser(uid, hm, true);
            if (result){
                model.addAttribute("success", "profile successfully updated");
            } else {
                model.addAttribute("error", "error while updating profile");
            }
        }
        if(hm.containsKey("ban")){
           AdminService adminService = new AdminService();
           if(adminService.setBannedStatus(uid, true)){
               model.addAttribute("success", "This User is been banned");
           }
           else{
               model.addAttribute("error", "This User is already banned");
           }
        }
        if(hm.containsKey("unban")){
            AdminService adminService = new AdminService();
            if(adminService.setBannedStatus(uid, false)){
                model.addAttribute("success", "This User is been unbanned");
            }
            else{
                model.addAttribute("error", "This User is not banned, YET");
            }
        }
        if(hm.containsKey("follow")){
            if(!userService.follow(hm.get("follow"), uid))
                model.addAttribute("info", "you already follow this user");
            else{
                model.addAttribute("success", "Successfully followed this user");
            }
        }
        else if(hm.containsKey("unfollow")){
            if(!userService.unfollow(hm.get("unfollow"), uid))
                model.addAttribute("info", "you already don't follow this user");
            else{
                model.addAttribute("success", "Successfully unfollowed this user");
            }
        }
        BaseUserDTO baseUser = (BaseUserDTO) userService.getUser(page, uid);
        model.addAttribute("user", baseUser);
        if(baseUser instanceof TopCriticDTO){
            model.addAttribute("numberOfFollowers", userService.getFollowers(baseUser.getId().toString()));
        }

        model.addAttribute("page", page);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "user";
    }
    @GetMapping("/preferred_genres/{username}")
    public  String mostLikedGenresByUser(Model model,
                                         @PathVariable(value = "username") String username,
                                         HttpSession session){
        if(session.getAttribute("credentials")==null){
            return "login";
        }
        if((session.getAttribute("credentials") instanceof AdminDTO)){
            model.addAttribute("redirect", "/admin-panel");
            return "movie";
        }
        BaseUserDTO userDTO = (BaseUserDTO) session.getAttribute("credentials");
        if(!userDTO.getUsername().equals(username)){
            model.addAttribute("go_to_user", userDTO.getId().toString());
            return "movie";
        }
        UserService userService = new UserService();
        if(username==null){
            username="";
        }
        model.addAttribute("genres",userService.getGenresLike(username).getEntries());
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "preferred-genres";
    }
    @RequestMapping("/suggested-top-critic/{username}")
    public  String suggestedTopCritic(Model model,
                                      HttpServletRequest request,
                                      @PathVariable(value = "username") String username,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      HttpSession session){
        if(session.getAttribute("credentials")==null){
            return "login";
        }
        HashMap<String, String> hm = extractRequest(request);
        UserService userService = new UserService();

        if((session.getAttribute("credentials") instanceof AdminDTO)){
            model.addAttribute("redirect", "/admin-panel");
            return "movie";
        }
        if((session.getAttribute("credentials") instanceof TopCriticDTO)){
            TopCriticDTO topCriticDTO = (TopCriticDTO) session.getAttribute("credentials");
            model.addAttribute("go_to_user", topCriticDTO.getId().toString());
            return "movie";
        }
        UserDTO userDTO = (UserDTO) session.getAttribute("credentials");
        if(!userDTO.getUsername().equals(username)){
            model.addAttribute("go_to_user", userDTO.getId().toString());
            return "movie";
        }

        if(username==null){
            username="";
        }
        if (page<=0){
            page=0;
        }
        if(hm.containsKey("follow")){
            if(!userService.follow(userDTO.getId().toString(), hm.get("follow"))) {
                model.addAttribute("error", "Failed to follow this Top Critic!");
            }
            else{
                model.addAttribute("success", "Successfully followed this Top Critic");
            }
        }
        model.addAttribute("suggestions",userService.getTopCriticSuggestions(new User(userDTO),page).getEntries());
        model.addAttribute("page",page);
        model.addAttribute("username",userDTO.getUsername());
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "suggested-top-critic";
    }

    @GetMapping("/recommendations")
    public String recommendations(Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  HttpSession session){
        model.addAttribute("credentials", session.getAttribute("credentials"));
        model.addAttribute("page", String.format("page: %d", page));
        return "recommendations";
    }

    @RequestMapping("/admin-panel")
    public  String adminPanel(Model model,
                              HttpServletRequest request,
                              HttpSession session){
        if(!(session.getAttribute("credentials") instanceof AdminDTO)){
            return "login";
        }
        HashMap<String, String> hm = extractRequest(request);
        if(hm.containsKey("searchUser")){
            model.addAttribute("searchUser", hm.get("searchUser"));
            if(!hm.get("searchUser").isEmpty()) {
                AdminService adminService = new AdminService();
                PageDTO<RegisteredUserDTO> userList = adminService.listUserPage(0, hm);
                System.out.println(userList.getTotalCount());
                if(userList.getTotalCount()>0) {
                    System.out.println(userList.getEntries());
                    model.addAttribute("userList", userList.getEntries());
                }
                else{
                    model.addAttribute("info", "no result for " + hm.get("searchUser"));
                }
            }
        }


        return "admin-panel";
    }
    @GetMapping("/admin-panel/populationByGeneration")
    public  String adminPanelPopulation(Model model,
                                        @RequestParam(value = "offset", defaultValue = "5") int offset,
                                        HttpSession session){
        AdminService adminService = new AdminService();
        model.addAttribute("generation",adminService.getPopulationByGeneration(offset).getEntries());
        model.addAttribute("offset",offset);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "populationByGeneration";
    }
    @GetMapping("/HOFProductionHouses")
    public  String HOFProductionHouses(Model model,
                                       @RequestParam(value = "sort", defaultValue = "top_critic") String sort,
                                       @RequestParam(value = "min_movie_count", defaultValue = "5") int min_movie_count,
                                       HttpSession session){
        MovieService movieService=new MovieService();
        if(min_movie_count<=0){
            min_movie_count=5;
        }
        model.addAttribute("productionhouses",movieService.getHOFProductionHouses(sort,min_movie_count).getEntries());
        model.addAttribute("min_movie_count",min_movie_count);
        model.addAttribute("sort",sort);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "HOFProductionHouses";
    }
    @GetMapping("/HOFGenres")
    public  String HOFGenres(Model model,
                                       @RequestParam(value = "sort", defaultValue = "top_critic") String sort,
                                       @RequestParam(value = "min_movie_count", defaultValue = "5") int min_movie_count,
                                         HttpSession session){
        MovieService movieService=new MovieService();
        if(min_movie_count<=0){
            min_movie_count=5;
        }
        model.addAttribute("genres",movieService.getHOFGenres(sort,min_movie_count).getEntries());
        model.addAttribute("min_movie_count",min_movie_count);
        model.addAttribute("sort",sort);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "HOFGenres";
    }
    @GetMapping("/HOFYears")
    public  String HOFYears(Model model,
                             @RequestParam(value = "sort", defaultValue = "top_critic") String sort,
                             @RequestParam(value = "min_movie_count", defaultValue = "5") int min_movie_count,
                            HttpSession session){
        MovieService movieService=new MovieService();
        if(min_movie_count<=0){
            min_movie_count=5;
        }
        model.addAttribute("years",movieService.getHOFYears(sort,min_movie_count).getEntries());
        model.addAttribute("min_movie_count",min_movie_count);
        model.addAttribute("sort",sort);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "HOFYears";
    }
    @RequestMapping("/feed")
    public String userFeed (Model model,
                            HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            HttpSession session){
        System.out.println("credentials: " + session.getAttribute("credentials"));
        if(!(session.getAttribute("credentials") instanceof UserDTO)){
            return "login";
        }
        if (page < 0){
            page = 0;
        }
        UserService userService = new UserService();
        HashMap<String, String> hm = extractRequest(request);
        if(hm.containsKey("critic_id") && hm.containsKey("movieTitle")){
            String topCriticId= hm.get("critic_id");
            String movieTitle = hm.get("movieTitle");
            ArrayList<Object> movieAndIndex;
            movieAndIndex = userService.getReviewIndex(new ObjectId(topCriticId),movieTitle);
            if (movieAndIndex == null || movieAndIndex.size() == 0){
                model.addAttribute("error", "Can't find a review for this critic");
            } else {
                System.out.println(movieAndIndex.get(0) + " " + movieAndIndex.get(1));
                String urlPath = "/movie/" + movieAndIndex.get(0).toString() + "/" + movieAndIndex.get(1);
                model.addAttribute("redirect", urlPath);
                return "movie";
            }
        }
        BaseUser user = new User((UserDTO) session.getAttribute("credentials"));
        model.addAttribute("feed",userService.createUserFeed(user,page).getEntries());
        model.addAttribute("page",page);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "feed";
    }

    @GetMapping("/review-bombing/{mid}")
    public String checkReviewBombing (Model model,
                                      HttpServletRequest request,
                                      @PathVariable(value = "mid") String mid,
                                      @RequestParam(value = "month_count", defaultValue = "36") int month_count,
                                      HttpSession session) {
        if (!(session.getAttribute("credentials") instanceof AdminDTO)) {
            return "login";
        }
        if (month_count <= 0) {
            month_count = 24;
        }
        AdminService adminService = new AdminService();
        Movie movie = new Movie();
        movie.setId(new ObjectId(mid));
        MovieReviewBombingDTO movieReviewBombingDTO = adminService.checkReviewBombing(movie, month_count);
        if (movieReviewBombingDTO == null) {
            model.addAttribute("error", "No reviews were made in the last "+ month_count +" months. Please try with higher month number");
        }
        model.addAttribute("reviewBombing", movieReviewBombingDTO);
        model.addAttribute("month_count", month_count);
        return "review-bombing";
    }

    @GetMapping("/admin-panel/mostReviewUser")
    public String  mostReviewUser(Model model,
                                  HttpSession session){
        if(!(session.getAttribute("credentials") instanceof AdminDTO)){
            return "login";
        }
        AdminService adminService = new AdminService();
        model.addAttribute("credentials", session.getAttribute("credentials"));
        model.addAttribute("userList", adminService.getMostReviewUser());

        return "mostReviewUser";
    }

    @GetMapping("/admin-panel/mostFollowTopCritic")
    public String  mostFollowTopCritic(Model model,
                                  HttpSession session){
        if(!(session.getAttribute("credentials") instanceof AdminDTO)){
            return "login";
        }
        AdminService adminService = new AdminService();
        model.addAttribute("credentials", session.getAttribute("credentials"));
        model.addAttribute("userList", adminService.getFollowTopCritic());

        return "mostReviewUser";

    }
}
