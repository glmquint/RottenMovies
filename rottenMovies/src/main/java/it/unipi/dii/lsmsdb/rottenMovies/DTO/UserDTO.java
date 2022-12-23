package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;

import java.util.ArrayList;
import java.util.Date;

public class UserDTO extends BaseUserDTO{
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public UserDTO(){}
    public UserDTO (User user){
        this.id=user.getId();
        this.username=user.getUsername();
        this.password=user.getPassword();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.registrationDate=user.getRegistrationDate();
        ArrayList<Review> last3review = user.getLast3Reviews();
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
        ArrayList<SimplyfiedReview> allreviews = user.getReviews();
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

    }
}
