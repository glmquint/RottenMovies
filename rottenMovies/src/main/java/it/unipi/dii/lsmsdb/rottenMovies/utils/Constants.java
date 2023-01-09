package it.unipi.dii.lsmsdb.rottenMovies.utils;

public class Constants {
    public static final String NEO4J_CONNECTION_STRING = "neo4j://172.16.5.27:7687"; // "neo4j://localhost:7687" // "neo4j://172.16.5.27:7687"
    public static final String NEO4J_USERNAME = "neo4j";
    public static final String NEO4J_PASSWORD = "password";
    public static final String MONGO_CONNECTION_STRING = "mongodb://172.16.5.26:27017"; // "mongodb://localhost:27017" // "mongodb://172.16.5.26:27017"
    public static final String MONGO_DATABASE_STRING = "rottenMovies";
    public static final String NEO4J_DATABASE_STRING = "rottenmoviesgraphdb";

    public static final String COLLECTION_STRING_MOVIE = "movie";
    public static final String COLLECTION_STRING_USER = "user";
    public static final int MOVIES_PER_PAGE = 30;
    public static final int REVIEWS_PER_PAGE = 15;
    public static final int USERS_PER_PAGE = 5;
    //public static final String USERS_MARKED_AS_DELETED="[[IS_GOING_TO_BE_DELETED]]";
    public static final int REVIEWS_IN_FEED = 10;
    public static final int SUGGESTIONS_IN_FEED = 10;
    public static final String DELETED_USER = "Deleted User";
    public static final String DELETED_REVIEW = "Deleted Review";
    public static final int HALL_OF_FAME_ELEMENT_NUMBERS = 10;
    public static final int MAXIMUM_NUMBER_OF_PERSONNEL = 20;

}
