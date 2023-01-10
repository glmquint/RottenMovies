package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import org.bson.types.ObjectId;

/**
 * <class>UserLeaderboardDTO</class> is the container used to pass information for
 * most followed top critic and most active user functionalities between
 * the service and presentation layer
 */
public class UserLeaderboardDTO {
    protected ObjectId id;
    protected String username;
    protected int counter;
    protected String type;

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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserLeaderboardDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", counter=" + counter +
                ", type='" + type + '\'' +
                '}';
    }
}
