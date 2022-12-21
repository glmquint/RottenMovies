package it.unipi.dii.lsmsdb.rottenMovies.DTO;

public class ReviewMovieDTO extends ReviewDTO{
    private String criticName;

    public String getCriticName() {
        return criticName;
    }

    public void setCriticName(String criticName) {
        this.criticName = criticName;
    }
}
