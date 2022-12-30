package it.unipi.dii.lsmsdb.rottenMovies.controller;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PageDTO;
import it.unipi.dii.lsmsdb.rottenMovies.services.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        return "register";
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
                                @RequestParam(value = "page", defaultValue = "0") int page, // comment pagination
                                @PathVariable(value = "mid") String mid){
        int comment_index = -1;
        model.addAttribute("page", String.format("page: %d", page));
        model.addAttribute("c_idx", String.format("comment_index: %d", comment_index));
        model.addAttribute("movie", String.format("movie: %s", mid));
        return "movie";
    }

    @GetMapping("/movie/{mid}/{comment_index}")
    public  String select_movie_comemnt(Model model,
                                        @PathVariable(value = "mid") String mid,
                                        @PathVariable(value = "comment_index") int comment_index){
        int page = -1;
        model.addAttribute("page", String.format("page: %d", page));
        model.addAttribute("c_idx", String.format("comment_index: %d", comment_index));
        model.addAttribute("movie", String.format("movie: %s", mid));
        return "movie";
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