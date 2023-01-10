package it.unipi.dii.lsmsdb.rottenMovies.DTO;
/**
 * <class>GenresLikeDTO</class> is the container used to pass data regarding genres likeness between
 * the service and presentation layer
 */
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
