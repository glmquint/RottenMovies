package it.unipi.dii.lsmsdb.rottenMovies.models;

import java.util.Date;

public class BaseUser extends User{
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
}
