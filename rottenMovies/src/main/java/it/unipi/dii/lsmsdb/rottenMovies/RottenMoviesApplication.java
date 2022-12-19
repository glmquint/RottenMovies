package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

//@SpringBootApplication
public class RottenMoviesApplication {

	public  static void main(String[] args) throws JsonProcessingException {
		MovieDAO test = DAOLocator.getMovieDAO();
		System.out.println(test.searchByTitle("Evidence"));
		for (Movie movie : test.searchByYear(1929)){
			System.out.println(movie);
		}
	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
