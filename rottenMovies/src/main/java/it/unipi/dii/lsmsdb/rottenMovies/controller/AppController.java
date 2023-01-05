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
import org.springframework.web.bind.annotation.*;

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
            return "login"; // TODO: change to feed
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
        return "login"; // TODO: change to feed
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

    @RequestMapping("/movie/{mid}")
    public  String select_movie(Model model,
                                HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @PathVariable(value = "mid") String mid,
                                HttpSession session){
        System.out.println("credentials: " + session.getAttribute("credentials"));
        HashMap<String, String> hm = extractRequest(request);
        System.out.println(hm);
        MovieService movieService = new MovieService();
        boolean result = false;
        if (hm.containsKey("admin_operation")){
            result = movieService.modifyMovie(mid, hm);
            if (result){
                model.addAttribute("success", "movie successfully update");
            } else {
                model.addAttribute("error", "error while updating movie");
            }
//            if (hm.get("admin_operation").equals("update")){
//                boolean result = movieService.updateMovie(hm);
//                if (result){
//                    model.addAttribute("success", "movie successfully update");
//                } else {
//                    model.addAttribute("error", "error while updating movie");
//                }
//            } else if (hm.get("admin_operation").equals("delete")){
//                boolean result = movieService.deleteMovie(mid);
//                if (result){
//                    model.addAttribute("success", "movie successfully removed");
//                } else {
//                    model.addAttribute("error", "error while removing movie");
//                }
//                return "movie";
//            }
        }
        if (page < 0){
            page = 0;
        }
        model.addAttribute("movie", movieService.getMovie(page, mid, -1));
        model.addAttribute("page", page);
        model.addAttribute("credentials", session.getAttribute("credentials"));
        return "movie";
    }

    @GetMapping("/movie/{mid}/{comment_index}")
    public  String select_movie_comment(Model model,
                                        @PathVariable(value = "mid") String mid,
                                        @PathVariable(value = "comment_index") int comment_index){
        MovieService movieService = new MovieService();
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
        model.addAttribute("user", userService.getUser(page, uid));

        model.addAttribute("page", page);
        model.addAttribute("credentials", session.getAttribute("credentials"));
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

}
