package it.unipi.dii.lsmsdb.rottenMovies;

import it.unipi.dii.lsmsdb.rottenMovies.backend.base.MongoDBConnector;
import it.unipi.dii.lsmsdb.rottenMovies.backend.mongoDB.MovieMongoDB_DAO;

//@SpringBootApplication
public class RottenMoviesApplication {

	public  static void main(String[] args){
		//MongoDBConnector test = new MongoDBConnector();
		//test.testConnection();
		//test.returnMovieByTitle("Evidence");
		MovieMongoDB_DAO test = new MovieMongoDB_DAO();
		System.out.println(test.searchByTitle("Evidence"));
	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
