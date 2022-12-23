package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseUserDTO extends RegisteredUserDTO{
    protected String firstName;
    protected String lastName;
    protected Date registrationDate;
    protected ArrayList<ReviewUserDTO> last3Reviews;
    protected ArrayList<SimplyfiedReviewDTO> reviews;

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

    public ArrayList<ReviewUserDTO> getLast3Reviews() {
        return last3Reviews;
    }

    public void setLast3Reviews(ArrayList<ReviewUserDTO> last3Reviews) {
        this.last3Reviews = last3Reviews;
    }

    public ArrayList<SimplyfiedReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<SimplyfiedReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
