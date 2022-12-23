package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

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
}
