package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.BaseUser;
import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;

import java.util.ArrayList;
import java.util.Date;

/**
 * <class>BaseUserDTO</class> is the container used to pass data regarding a BaseUser between
 * the service and presentation layer
 */
public abstract class BaseUserDTO extends RegisteredUserDTO{
    protected String firstName;
    protected String lastName;
    protected Date registrationDate;
    protected ArrayList<ReviewUserDTO> last3Reviews;

    protected ArrayList<SimplyfiedReviewDTO> reviews;
    protected boolean isBanned;

    protected BaseUserDTO(){super();}
    protected BaseUserDTO(BaseUser b){
        super(b);
        this.firstName=b.getFirstName();
        this.lastName=b.getLastName();
        this.registrationDate=b.getRegistrationDate();
        ArrayList<Review> last3review = b.getLast3Reviews();
        ArrayList<ReviewUserDTO> reviews = null;
        if (last3review != null){
            reviews = new ArrayList<ReviewUserDTO>();
            ReviewUserDTO review = null;
            for (Review r : last3review) {
                review = new ReviewUserDTO(r);
                reviews.add(review);
            }
        }
        this.last3Reviews=reviews;
        ArrayList<SimplyfiedReview> allreviews = b.getReviews();
        ArrayList<SimplyfiedReviewDTO> reviewsdto = null;
        if (allreviews != null){
            reviewsdto = new ArrayList<SimplyfiedReviewDTO>();
            SimplyfiedReviewDTO reviewL = null;
            for (SimplyfiedReview r : allreviews) {
                reviewL = new SimplyfiedReviewDTO(r);
                reviewsdto.add(reviewL);
            }
        }
        this.reviews=reviewsdto;
        this.isBanned=b.isBanned();
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

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
