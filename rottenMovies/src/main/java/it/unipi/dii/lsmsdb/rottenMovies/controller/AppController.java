package it.unipi.dii.lsmsdb.rottenMovies.controller;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.services.MovieService;
import it.unipi.dii.lsmsdb.rottenMovies.services.UserService;
import it.unipi.dii.lsmsdb.rottenMovies.services.UserService;
import it.unipi.dii.lsmsdb.rottenMovies.utils.MD5;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String index(Model model){
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model, HttpSession session, HttpServletRequest request){
        //session.setAttribute("credentials", session.getAttribute("credentials"));
        System.out.println("credentials: " + session.getAttribute("credentials"));
        if (session.getAttribute("credentials")!=null) {
            model.addAttribute("info", "you're already logged in");
            model.addAttribute("credentials", session.getAttribute("credentials"));
            return "login"; // TODO: change to feed
        }
        UserService userService = new UserService();
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        RegisteredUserDTO baseuserdto = null;
        if (!hm.containsKey("username") || !hm.containsKey("password")) {
            return "login";
        }
        baseuserdto = userService.authenticate(hm.get("username"), MD5.getMd5(hm.get("password")));
        if (baseuserdto == null) {
            //hm.put("error", "invalid username or password");
            model.addAttribute("error", "invalid username or password");
            return "login";
        }
        session.setAttribute("credentials", baseuserdto);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        model.addAttribute("success", "login successful");
        return "login"; // TODO: change to feed
    }

    @RequestMapping("/register")
    public String register(Model model, HttpSession session, HttpServletRequest request){
        HttpSession newSession = request.getSession(); // create session
        //newSession.setAttribute("test", new UserDTO());
        return "register";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        session.invalidate();
        return "logout";
    }

    @GetMapping("/movie")
    public  String explore_movie(Model model,
                                 HttpServletRequest request){
        //System.out.println("requested page");
        //System.out.println(page);
        //model.addAttribute("page", page);
        MovieService movieService = new MovieService();
        HashMap<String, String> hm = extractRequest(request);
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

    @GetMapping("/movie/{mid}")
    public  String select_movie(Model model,
                                //HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @PathVariable(value = "mid") String mid){
        MovieService movieService = new MovieService();
        if (page < 0){
            page = 0;
        }
        model.addAttribute("movie", movieService.getMovie(page, mid, -1));
        model.addAttribute("page", page);
        return "movie";
    }

    @GetMapping("/movie/{mid}/{comment_index}")
    public  String select_movie_comemnt(Model model,
                                        @PathVariable(value = "mid") String mid,
                                        @PathVariable(value = "comment_index") int comment_index){
        MovieService movieService = new MovieService();
        model.addAttribute("movie", movieService.getMovie(0, mid, comment_index));
        return "movie";
    }

    @GetMapping("/user/{uid}")
    public  String select_user(Model model,
                                //HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @PathVariable(value = "uid") String uid){
        UserService userService = new UserService();
        if (page < 0){
            page = 0;
        }
        model.addAttribute("user", userService.getUser(page, uid));
        model.addAttribute("page", page);
        return "user";
    }

    @GetMapping("/feed")
    public String feed(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page){
        model.addAttribute("page", String.format("page: %d", page));
        return "feed";
    }

    @GetMapping("/recommendations")
    public String recommendations(Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page){
        model.addAttribute("page", String.format("page: %d", page));
        return "recommendations";
    }

    @GetMapping("/admin-panel")
    public  String adminPanel(Model model){
        return "admin-panel";
    }
    @GetMapping("/HOFProductionHouses")
    public  String HOFProductionHouses(Model model,
                                       @RequestParam(value = "sort", defaultValue = "top_critic") String sort,
                                       @RequestParam(value = "min_movie_count", defaultValue = "5") int min_movie_count){
        MovieService movieService=new MovieService();
        if(min_movie_count<=0){
            min_movie_count=5;
        }
        model.addAttribute("productionhouses",movieService.getHOFProductionHouses(sort,min_movie_count).getEntries());
        model.addAttribute("min_movie_count",min_movie_count);
        model.addAttribute("sort",sort);
        return "HOFProductionHouses";
    }
    @GetMapping("/HOFGenres")
    public  String HOFGenres(Model model,
                                       @RequestParam(value = "sort", defaultValue = "top_critic") String sort,
                                       @RequestParam(value = "min_movie_count", defaultValue = "5") int min_movie_count){
        MovieService movieService=new MovieService();
        if(min_movie_count<=0){
            min_movie_count=5;
        }
        model.addAttribute("genres",movieService.getHOFGenres(sort,min_movie_count).getEntries());
        model.addAttribute("min_movie_count",min_movie_count);
        model.addAttribute("sort",sort);
        return "HOFGenres";
    }


}
