package it.unipi.dii.lsmsdb.rottenMovies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB.MovieMongoDB_DAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.MovieDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import org.bson.Document;

import java.util.ArrayList;

//@SpringBootApplication
public class RottenMoviesApplication {

	public  static void main(String[] args) throws JsonProcessingException {
		/*
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.MONGO)){
			BaseUser base=testUser.getByUsername("Abbie Bernstein");
			System.out.println(base);
			System.out.println("=========================");
			UserDTO user = new UserDTO((User) base);
			System.out.println(new User(user));
			System.out.println("=========================");
			System.out.println(new User());
			System.out.println("=========================");

			base=testUser.getByUsername("Ernest Hardy");
			System.out.println(base);
			System.out.println("=========================");

			TopCriticDTO top = new TopCriticDTO((TopCritic) base);
			System.out.println(new TopCritic(top));
			System.out.println("=========================");
			System.out.println(new TopCritic());
			System.out.println("=========================");

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

		try(MovieDAO moviedao = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)){
			MovieDTO newmovie = new MovieDTO();
			newmovie.setPrimaryTitle("test nuovo movie 1");
			((MovieMongoDB_DAO) moviedao).insert(newmovie);
			System.out.println("insert done 1");
			newmovie.setPrimaryTitle("test nuovo movie 2");
			((MovieMongoDB_DAO) moviedao).insert(newmovie);
			System.out.println("insert done 2");
			((MovieMongoDB_DAO) moviedao).queryBuildSearchByTitleContains("test nuovo");
			ArrayList<MovieDTO> movies = ((MovieMongoDB_DAO) moviedao).executeSearchQuery(-1);
			for (MovieDTO movie : movies){
				System.out.println(new Movie(movie));
			}
			((MovieMongoDB_DAO) moviedao).executeDeleteQuery();
		} catch (Exception e){
			System.err.println(e.getStackTrace());
		}
/*
		try(MovieDAO testMovie = DAOLocator.getMovieDAO(DataRepositoryEnum.MONGO)){
		*/
			/*
			MongoCursor<Document> iter = testMovie.getCollection().find(((MovieMongoDB_DAO)testMovie).queryBuildSearchByTitleContains("avengers").getQuery()).iterator();
			ObjectMapper mapper = new ObjectMapper();
			while (iter.hasNext()){
				System.out.println(mapper.readValue(iter.next().toJson(), Movie.class));
				System.out.println("=========================");
			}

			 */

/*
			((MovieMongoDB_DAO) testMovie).queryBuildSearchByTitleContains("avengers");
			((MovieMongoDB_DAO) testMovie).queryBuildSearchByYearRange(2018, 2019);
			ArrayList<MovieDTO> movies = ((MovieMongoDB_DAO) testMovie).executeSearchQuery(0);
			for (MovieDTO movie : movies){
				System.out.println(new Movie(movie));
			}*/

			/*
			ArrayList<MovieDTO> list = testMovie.searchByYearRange(1929,1930);
			for(MovieDTO m:list){
				System.out.println(new Movie(m));
			}
*/
			//System.out.println(testMovie.insert(movie));
			//Movie movie=testMovie.searchById(new ObjectId("63a484cf9b999919abc1921d"));
			//System.out.println(testMovie.delete(movie));
			//movie.setRuntimeMinutes(50);
			//testMovie.update(movie);
			//testMovie.delete(movie);
			//testMovie.insert(movie);
/*
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

		/*
		try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j)){
			 System.out.println(testUser.createBaseUser("Fabio Piras", false));
			System.out.println(testUser.createBaseUser("Giacomo Volpi", true));
			System.out.println(testUser.followTopCritic("Fabio Piras", "Giacomo Volpi"));
			//System.out.println(testUser.unfollowTopCritic("Fabio Piras", "Giacomo Volpi"));
			System.out.println(testUser.deleteBaseUser("Fabio Piras", false));


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


			//System.out.println(testUser.insertBaseUser(user));

		}catch (DAOException e){
			System.out.println("DAOExeption: wrong database queried: " + e.getMessage());
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception during testing: " + e.getMessage());
			e.printStackTrace();
		}
		*/


	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
