package it.unipi.dii.lsmsdb.rottenMovies.models;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private Date registrationDate;
    private boolean isTopCritic;
    private ArrayList<Review> last3Reviews;
    private ArrayList<String> reviews;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isTopCritic() {
        return isTopCritic;
    }

    public void setTopCritic(boolean topCritic) {
        isTopCritic = topCritic;
    }

    public ArrayList<Review> getLast3Reviews() {
        return last3Reviews;
    }

    public void setLast3Reviews(ArrayList<Review> last3Reviews) {
        this.last3Reviews = last3Reviews;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }
}
