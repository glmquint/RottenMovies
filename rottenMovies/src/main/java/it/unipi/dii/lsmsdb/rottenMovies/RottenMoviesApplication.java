package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

//@SpringBootApplication
public class RottenMoviesApplication {

	public  static void main(String[] args) throws JsonProcessingException {
		//MovieDAO testMovie = DAOLocator.getMovieDAO();
		//System.out.println(test.searchByTitle("Evidence"));
		/*for(int i  = 2001; i<2022; ++i) {
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
		/*Movie evidence=testMovie.searchByTitle("Evidence");
		System.out.println(evidence);
		evidence.setRuntimeMinutes((Object)100);
		System.out.println(evidence);
		testMovie.updateMovie(evidence);*/

		//test.deleteMovie("The Midnight Man");


		/*
		System.out.println(testUser.getUserByUserName("Mark R. Leeper"));
		for (BaseUser baseUser : testUser.getUser()) {
			System.out.println(baseUser);
			System.out.println("=============================");
		}

		 */
		try (BaseUserDAO testUser = DAOLocator.getBaseUserDAO()){
			//DAOException test
			//BaseUser baseUser = testUser.getMostReviewUser();
			BaseUser usr = testUser.getUserByUserName("Abbie Bernstein");
			System.out.println(usr);
			usr.setFirstName("AbbieNew");
			usr.setLastName("BernsteinNew");
			System.out.println(usr);
			testUser.insertBaseUser(usr);
			usr.setLastName("BernsteinModified");
			testUser.modifyBaseUser(usr);
		}
		catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}

	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
