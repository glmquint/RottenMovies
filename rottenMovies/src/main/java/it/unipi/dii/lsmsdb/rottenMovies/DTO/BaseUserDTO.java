package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;

import java.util.Date;
import java.util.List;

public abstract class BaseUserDTO {
    private String firstName;
    private String lastName;
    private Date registrationDate;
    private List<Review> last3Reviews;
    private List<SimplyfiedReview> reviews;

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

    public List<Review> getLast3Reviews() {
        return last3Reviews;
    }

    public void setLast3Reviews(List<Review> last3Reviews) {
        this.last3Reviews = last3Reviews;
    }

    public List<SimplyfiedReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<SimplyfiedReview> reviews) {
        this.reviews = reviews;
    }
}
