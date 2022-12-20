package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

//@SpringBootApplication
public class RottenMoviesApplication {

	public static void main(String[] args) throws JsonProcessingException {
		/*MovieDAO test = DAOLocator.getMovieDAO();
		System.out.println(test.searchByTitle("Evidence"));
		for(int i  = 2001; i<2022; ++i) {
			for (Movie movie : test.searchByYear(i)) {
				//System.out.println(movie);
			}
		}*/
		//test.searchByYearRange(2000, 2000);
		/*
		for(Movie movie : test.searchByUserRatings(10, false)){
			System.out.println(movie.lessDataString());
			System.out.println("=============================");
		}

		 */

		//test.deleteMovie("The Midnight Man");

		BaseUserDAO test = DAOLocator.getBaseUserDAO();
		//System.out.println(test.getUserByUserName("Mark R. Leeper"));
		for (BaseUser baseUser : test.getUser()) {
			System.out.println(baseUser);
			System.out.println("=============================");
		}
	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
