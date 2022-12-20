package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class BaseUser extends User{
    @JsonProperty("birthday_date")
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Object birthdayDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(birthdayDate instanceof LinkedHashMap<?,?>)
            try {
                LinkedHashMap link = (LinkedHashMap)birthdayDate;
                //System.out.println(link.get("$date"));
                //System.out.println(link.get("$numberLong"));
                //System.out.println(link.get("$date").getClass());
                if(link.get("$date")!=null) {
                    if(link.get("$date") instanceof LinkedHashMap<?,?>) {
                        this.birthdayDate = new Date(1970, 1, 1);

                    }
                    else{
                        this.birthdayDate = formatter.parse(link.get("$date").toString());
                    }
                }
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        else if (birthdayDate instanceof String) {
            try {
                this.birthdayDate = formatter.parse((String) birthdayDate);
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else if (birthdayDate instanceof Date){
            this.birthdayDate=(Date)birthdayDate;
        }
    }

    @Override
    public String toString() {

        return "BaseUser{" + '\n' +
                "username " + getUsername() + '\n' +
                "password " + getPassword() + '\n' +
                "first_name " + getFirstName() + '\n' +
                "last_name " + getLastName() + '\n' +
                "birthday_date " +this.birthdayDate + '\n' +
                "last_3_review " + getLast3Reviews() + '\n' +
                "reviews " + getReviews() + '\n' +
                "}";
    }
}
