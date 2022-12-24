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

		mainGiacomo();
	}
	public static void mainGiacomo(){
		System.out.println("My main");
	}
	/*public static void main(String[] args) {
		SpringApplication.run(RottenMoviesApplication.class, args);
	}*/

}
