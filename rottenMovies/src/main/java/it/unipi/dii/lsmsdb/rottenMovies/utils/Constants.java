package it.unipi.dii.lsmsdb.rottenMovies.utils;

public class Constants {
    public static final String CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String DATABASE_STRING = "rottenMovies";

    public static final String COLLECTION_STRING_MOVIE = "movie";
    public static final String COLLECTION_STRING_USER = "user";
    public static final int MOVIES_PER_PAGE = 30;
    public static final int REVIEWS_PER_PAGE = 15;
    public static final int USERS_PER_PAGE = 5;
    public static final String USERS_MARKED_AS_DELETED="[[IS_GOING_TO_BE_DELETED]]";
    public static final int REVIEWS_IN_FEED = 10;
    public static final int SUGGESTIONS_IN_FEED = 5;
    public static final String DELETED_USER = "Deleted User";
    public static final String DELETED_REVIEW = "Deleted Review";
    public static final int MAXIMUM_NUMBER_OF_GENRES = 10;

}
