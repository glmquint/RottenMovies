package it.unipi.dii.lsmsdb.rottenMovies.backend.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.models.Movie;

public interface MovieDAO {
    Movie searchByTitle(String title);
    Movie searchByYear(int year);
    Movie searchByTopRatings(int rating);
    Movie searchByUserRatings(int rating);
}
