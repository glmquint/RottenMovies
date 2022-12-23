package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Review;
import it.unipi.dii.lsmsdb.rottenMovies.models.SimplyfiedReview;
import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;

import java.util.ArrayList;

public class TopCriticDTO extends BaseUserDTO{
    private int follower_count;
    public TopCriticDTO(){}
    public TopCriticDTO(TopCritic user){
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
        this.follower_count=user.getFollower_count();
    }
    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }


}
