package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import org.bson.types.ObjectId;

public class TopCriticSuggestionDTO {
    ObjectId id;
    private String username;
    private int rate;

    public TopCriticSuggestionDTO(String id, String username, int rate) {
        this.id = new ObjectId(id);
        this.username = username;
        this.rate = rate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRate() {
        return rate;
    }


    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "TopCriticSuggestionDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", rate=" + rate +
                '}';
    }
}
