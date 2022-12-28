package it.unipi.dii.lsmsdb.rottenMovies.DTO;

public class TopCriticSuggestionDTO {
    private String username;
    private double rate;

    public TopCriticSuggestionDTO(String username, double rate) {
        this.username = username;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "TopCriticSuggestionDTO{" +
                "username='" + username + '\'' +
                ", rate=" + rate +
                '}';
    }
}
