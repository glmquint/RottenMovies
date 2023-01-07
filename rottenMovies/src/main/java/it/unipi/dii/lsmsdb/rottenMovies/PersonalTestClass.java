package it.unipi.dii.lsmsdb.rottenMovies;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.DAOLocator;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.enums.DataRepositoryEnum;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.BaseUserDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.MovieDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.ReviewDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.*;
import it.unipi.dii.lsmsdb.rottenMovies.controller.AppController;
import it.unipi.dii.lsmsdb.rottenMovies.models.*;
import org.bson.types.ObjectId;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.springframework.boot.SpringApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalTestClass {
    public void testMain() {

        SpringApplication.run(AppController.class);
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
//        try(BaseUserDAO testUser = DAOLocator.getBaseUserDAO(DataRepositoryEnum.NEO4j);
//            MovieDAO testMovie = DAOLocator.getMovieDAO(DataRepositoryEnum.NEO4j);
//            ReviewDAO testReview = DAOLocator.getReviewDAO(DataRepositoryEnum.NEO4j);
//            AdminDAO testAdmin = DAOLocator.getAdminDAO(DataRepositoryEnum.NEO4j);
//        ){
//            ArrayList<UserLeaderboardDTO> list = testAdmin.getMostReviewUser();
//            for(UserLeaderboardDTO user : list)
//                System.out.println(user);
//            Movie movie = new Movie();
//            movie.setId(new ObjectId("63a4547e60cc233b767e40a3"));
//            movie.setPrimaryTitle("test");
//            System.out.println(testMovie.insert(movie));
//            movie.setPrimaryTitle("test2");
//            System.out.println(testMovie.update(movie));
//            testMovie.delete(movie);
            /*BaseUser baseUser1 = new User();
            baseUser1.setId(new ObjectId("63a4547e60cc233b767e40a3"));
            System.out.println(baseUser1.getId());

            BaseUser baseUser2 = new TopCritic();
            baseUser2.setId(new ObjectId("63a46dd860cc233b767e55dd"));
            System.out.println(baseUser2.getId());

            BaseUser baseUser3 = new TopCritic();
            baseUser3.setId(new ObjectId("63a4544160cc233b767e4094"));
            System.out.println(baseUser3.getId());

            System.out.println(testUser.checkIfFollows(baseUser1, baseUser2));
            System.out.println(testUser.checkIfFollows(baseUser1, baseUser3));*/
//            BaseUser baseUser = new User();
//            baseUser.setId(new ObjectId("deadbeefdeadbeefdeadbeef"));
//            baseUser.setUsername("Fabio");
//            System.out.println(baseUser.getId());
//            System.out.println(baseUser.getUsername());
//
//            BaseUser baseUser2 = new TopCritic();
//            baseUser2.setId(new ObjectId("deadbeefdeadbeefdeadbeef"));
//            baseUser2.setUsername("Giacomo");
//            System.out.println(baseUser2.getId());
//            System.out.println(baseUser2.getUsername());
//
//            System.out.println(testUser.unfollowTopCritic(baseUser, baseUser2));
//        }
//        catch(Exception e){
//            System.out.println("Exception during testing: " + e.getMessage());
//            e.printStackTrace();
//        }

            //Movie movie = new Movie();
            //movie.setIdString("deadbeefdeadbeefdeadbeef");
            //movie.setPrimaryTitle("Joker");
            //LocalDate date = LocalDate.of(2019,12,1);
            //System.out.println(testReview.checkReviewBombing(movie, 36));
           /* Movie movie = new Movie();
            movie.setIdString("deadbeefdeadbeefdeadbeef");
            movie.setPrimaryTitle("test");

            BaseUser baseUser = new TopCritic();
            baseUser.setIdString("deadbeefdeadbeefdeadbeef");
            baseUser.setUsername("Fabio");
            System.out.println(baseUser.getId());
            System.out.println(baseUser.getUsername());

            BaseUser baseUser2 = new User();
            baseUser2.setIdString("deadbeefdeadbeefdeadbeef");
            baseUser2.setUsername("Giacomo");
            System.out.println(baseUser2.getId());
            System.out.println(baseUser2.getUsername());

            BaseUser baseUserDennis = new User();
            baseUserDennis.setIdString("63a4549f60cc233b767e40a9");
            baseUserDennis.setUsername("Dennis Schwartz");
            System.out.println(baseUserDennis.getId());
            System.out.println(baseUserDennis.getUsername());

            */
            /*List<ReviewFeedDTO> feed = testUser.getFeed(baseUserDennis, 0);
            for(ReviewFeedDTO reviewFeed : feed){
                System.out.println(reviewFeed);
            }
             */

            /*List<TopCriticSuggestionDTO> feed = testUser.getSuggestion(baseUserDennis, 0);
            System.out.println(feed);
            for(TopCriticSuggestionDTO suggestionFeed : feed){
                System.out.println(suggestionFeed);
            }

            Review review = new Review();
            review.setMovie("test");
            review.setReviewContent("content");
            review.setReviewDate(Date.valueOf(LocalDate.now()));
            review.setReviewType("Fresh");
            review.setCriticName("Fabio");

             */


            //System.out.println(testUser.insert(baseUser));
            //System.out.println(testUser.insert(baseUser2));
            //System.out.println(testMovie.insert(movie));
            //System.out.println(testReview.reviewMovie(baseUser,review));
            //System.out.println(testReview.delete(review));
            //System.out.println(testUser.delete(baseUser));
            //System.out.println(testUser.followTopCritic(baseUser2, baseUser));
            //System.out.println(testUser.unfollowTopCritic(baseUser2, baseUser));
            //System.out.println(testUser.getFeed(baseUserDennis,0));
            //System.out.println(movie.getId());
            //System.out.println(testMovie.insert(movie));
            //System.out.println(testMovie.delete(movie));
            //System.out.println(testReview.reviewMovieNeo4j("Volpi", "test", "content", Date.valueOf(LocalDate.now()), true));
            //System.out.println(testReview.deleteReviewNeo4j("Volpi", "test"));
            //System.out.println(testUser.deleteBaseUser("Piras"));
            //System.out.println(testUser.deleteBaseUser("Volpi"));
            //System.out.println(testMovie.deleteNeo4j("test"));
            //List<ReviewFeedDTO> feed = testReview.constructFeedNeo4j("63a4549f60cc233b767e40a9", 1);
            //for(ReviewFeedDTO reviewFeed : feed){
            //    System.out.println(reviewFeed);
            //}

         /*   ArrayList<UserDTO> feed = testUser.getMostReviewUser();
            for(UserDTO suggestionFeed : feed){
                System.out.println(suggestionFeed.getUsername());
            }
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
        }*/
    }
}

