package it.unipi.dii.lsmsdb.rottenMovies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import it.unipi.dii.lsmsdb.rottenMovies.dbConnection.MongoDBConnector;
//@SpringBootApplication
public class RottenMoviesApplication {

	public  static void main(String[] args){
		MongoDBConnector test = new MongoDBConnector();
		test.testConnection();
	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
