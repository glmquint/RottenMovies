package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@Controller
public class RottenMoviesApplication {

	public  static void main(String[] args)  {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}
	@GetMapping("/hello/{id}")
	public String hello(Model model,
						@RequestParam(value = "name", defaultValue = "World") String name,
						@RequestParam(value = "amount", defaultValue = "1") String amount,
						@PathVariable("projectId") int id) {
		model.addAttribute("return", String.format("Hello %s!", name));
		model.addAttribute("id", id);
		/*List<Movie> movies = new
		for (int i = 0; i < amount; i++) {

		}*/
		return "hello";
	}

	@GetMapping("/")
	public String index(Model model){
		return "index";
	}

	@GetMapping("/explore")
	public  String explore(Model model){
		return "explore";
	}

	@GetMapping("/login")
	public String login(Model model){
		return "login";
	}

	@GetMapping("/movie")
	public  String movie(Model model){
		return "movie";
	}

	@GetMapping("/admin-panel")
	public  String adminPanel(Model model){
		return "admin-panel";
	}

}
