package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import java.util.Date;

public class UserDTO extends BaseUserDTO{
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public UserDTO(){super();}
    public UserDTO (User user){
        super(user);
        this.birthdayDate=user.getBirthdayDate();
    }


}
