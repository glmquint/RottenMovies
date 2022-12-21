package it.unipi.dii.lsmsdb.rottenMovies.DTO;

public class ReviewUserDTO extends ReviewDTO{
    private String movie;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }
}
