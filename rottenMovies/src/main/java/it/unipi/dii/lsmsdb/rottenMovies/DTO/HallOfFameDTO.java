package it.unipi.dii.lsmsdb.rottenMovies.DTO;

/**
 * <class>HallOfFameDTO</class> is the container used to pass data regarding Hall of Fame statistics between
 * the service and presentation layer
 */

public class HallOfFameDTO {
    private String subject;
    private double top_critic_rating;
    private double user_rating;
    private int movie_count;

    public HallOfFameDTO() {}

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getTop_critic_rating() {
        return top_critic_rating;
    }

    public void setTop_critic_rating(double top_critic_rating) {
        this.top_critic_rating = top_critic_rating;
    }

    public double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(double user_rating) {
        this.user_rating = user_rating;
    }

    public int getMovie_count() {
        return movie_count;
    }

    public void setMovie_count(int movie_count) {
        this.movie_count = movie_count;
    }

    @Override
    public String toString() {
        return "HallOfFameDTO{" +
                "subject='" + subject + '\'' +
                ", top_critic_rating=" + top_critic_rating +
                ", user_rating=" + user_rating +
                ", movie_count=" + movie_count +
                '}';
    }
}
