package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.util.Date;

public class UserDTO extends BaseUserDTO{
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }
}
