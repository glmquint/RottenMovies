package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.BaseUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.ReviewUserDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.SimplyfiedReviewDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * <class>BaseUser</class> is the container in which data from the backend for BaseUser is mapped
 *                     RegisteredUser
 *                     /            \
 *                    /              \
 *                  Admin           BaseUser
 *                                  /       \
 *                                 /         \
 *                              User        TopCritic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseUser extends RegisteredUser {
    @JsonProperty("first_name")
    protected String firstName;
    @JsonProperty("last_name")
    protected String lastName;
    @JsonProperty("registration_date")
    protected Date registrationDate;
    @JsonProperty("last_3_reviews")
    protected ArrayList<Review> last3Reviews;
    @JsonProperty("reviews")
    protected ArrayList<SimplyfiedReview> reviews;
    @JsonProperty("isBanned")
    protected boolean isBanned;

    protected BaseUser (){super();}
    protected BaseUser(BaseUserDTO b){
        super(b);
        this.firstName=b.getFirstName();
        this.lastName=b.getLastName();
        this.registrationDate=b.getRegistrationDate();
        ArrayList<ReviewUserDTO> last3review = b.getLast3Reviews();
        ArrayList<Review> reviews = null;
        if (last3review != null){
            reviews = new ArrayList<Review>();
            Review review = null;
            for (ReviewUserDTO r : last3review) {
                review = new Review(r);
                reviews.add(review);
            }
        }
        this.last3Reviews=reviews;
        ArrayList<SimplyfiedReviewDTO> reviewsdto = b.getReviews();
        ArrayList<SimplyfiedReview> allreviews = null;
        if (reviewsdto != null){
            allreviews = new ArrayList<SimplyfiedReview>();
            SimplyfiedReview reviewL = null;
            for (SimplyfiedReviewDTO r : reviewsdto) {
                reviewL = new SimplyfiedReview(r);
                allreviews.add(reviewL);
            }
        }
        this.reviews=allreviews;
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

    public void setRegistrationDate(Object registrationDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(registrationDate instanceof LinkedHashMap<?,?>)
            try {
                LinkedHashMap link = (LinkedHashMap)registrationDate;
                //System.out.println(link.get("$date"));
                //System.out.println(link.get("$numberLong"));
                //System.out.println(link.get("$date").getClass());
                if(link.get("$date")!=null) {
                    if(link.get("$date") instanceof LinkedHashMap<?,?>) {
                        this.registrationDate = new Date(1970, 1, 1);
                    }
                    else{
                        this.registrationDate = formatter.parse(link.get("$date").toString());
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        else if (registrationDate instanceof String) {
            try {
                this.registrationDate = formatter.parse((String) registrationDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else if (registrationDate instanceof Date){
            this.registrationDate = ((Date)registrationDate);
        }
    }

    public ArrayList<Review> getLast3Reviews() {
        return last3Reviews;
    }

    public void setLast3Reviews(ArrayList<Review> last3Reviews) {
        this.last3Reviews = last3Reviews;
    }

    public ArrayList<SimplyfiedReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<SimplyfiedReview> reviews) {
        this.reviews = reviews;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
