package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PersonnelDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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
		/*
		try(BaseUserDAO baseUserDAO = DAOLocator.getBaseUserDAO()){
			User user= (User) baseUserDAO.getUserByUserName("Abbie Bernstein");
			System.out.println(user);
		}catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}

		 */
		/*
		try(MovieDAO testMovie = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)){
			MovieDTO movie = testMovie.searchByTitle("Evidence");
			System.out.println(new Movie(movie));
			/*
			//System.out.println(testMovie.insert(movie));
			//Movie movie=testMovie.searchById(new ObjectId("63a484cf9b999919abc1921d"));
			//System.out.println(testMovie.delete(movie));

			movie.setRuntimeMinutes(80);
			movie.setPersonnel(new ArrayList<PersonnelDTO>());
			movie.setYear((Integer)1980);
			//movie.setCriticConsensus("Helo");
			testMovie.update(movie);



		}catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}*/

		/*
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)){
			User user = (User) testUser.getByUsername("Dann Gire");
			System.out.println(user);
		}catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}
		*/
		/* TEST FOR NEO4J USER DAO
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
			 System.out.println(testUser.createBaseUser("Fabio Piras", false));
			System.out.println(testUser.createBaseUser("Giacomo Volpi", true));
			System.out.println(testUser.followTopCritic("Fabio Piras", "Giacomo Volpi"));
			//System.out.println(testUser.unfollowTopCritic("Fabio Piras", "Giacomo Volpi"));
			System.out.println(testUser.deleteBaseUser("Fabio Piras", false));

			/*
			TopCritic topCritic = (TopCritic) testUser.getByUsername("Ian Buckwalter");
			System.out.println(topCritic);
			user.setFirstName("ModifiedAbbie");
			user.setLastName("ModifiedBernstein");
			System.out.println(testUser.modify(user));
			topCritic.setFirstName("ModifiedIan");
			topCritic.setLastName("ModifiedBuckwalter");
			System.out.println(testUser.modify(topCritic));
			System.out.println(testUser.delete(topCritic));
			System.out.println(testUser.delete(user));
			*/

			//System.out.println(testUser.insertBaseUser(user));
	/*
		}catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}*/

		//uso questa funzione per eseguire i miei test così
		//da non intasare ulteriormente il main
		mainTestFabio();



	}

	private static void mainTestFabio() {
		/* TEST FOR THE USER NEO4J
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)) {
			System.out.println(testUser.createBaseUser("Piras", "Fabio Piras", false));
			System.out.println(testUser.createBaseUser("Volpi", "Giacomo Volpi", true));
			System.out.println(testUser.updateBaseUser("Volpi", "Volpi Giacomo"));
			//System.out.println(testUser.followTopCritic("Fabio Piras", "Giacomo Volpi"));
			//System.out.println(testUser.unfollowTopCritic("Fabio Piras", "Giacomo Volpi"));
			System.out.println(testUser.deleteBaseUser("Piras"));
			System.out.println(testUser.deleteBaseUser("Volpi"));
		}
		*/
		/*	TEST FOR THE MOVIE NEO4J
		try(MovieDAO testMovie = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j)){
			System.out.println(testMovie.insertNeo4j("test", "test"));
			System.out.println(testMovie.deleteNeo4j("test"));
			System.out.println(testMovie.updateNeo4j("test", "test2"));
		}
		*/
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j);
			MovieDAO testMovie = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j);
			ReviewDAO testReview = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j);
		){
			System.out.println(testUser.createBaseUser("Volpi", "Giacomo Volpi", true));
			System.out.println(testMovie.insertNeo4j("test", "test"));
			System.out.println(testReview.reviewMovie("Volpi", "test", "content", Date.valueOf(LocalDate.now()), true));
			System.out.println(testReview.deleteReviewNeo4j("Volpi", "test"));
			//System.out.println(testUser.deleteBaseUser("Piras"));
			//System.out.println(testUser.deleteBaseUser("Volpi"));
			//System.out.println(testMovie.deleteNeo4j("test"));
		}
		catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch (NoSuchRecordException e){
			System.out.println("Exception during testing: " + e.getMessage());
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
