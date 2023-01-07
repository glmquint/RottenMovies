package it.unipi.dii.lsmsdb.rottenMovies.DTO;

public class GenresLikeDTO {
    String genre;
    int count;

    public GenresLikeDTO() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "GenresLikeDTO{" +
                "genre='" + genre + '\'' +
                ", count=" + count +
                '}';
    }
}
