package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.AdminDTO;

public class Admin extends RegisteredUser {
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    public Admin(){
        super();
    }
    public Admin(AdminDTO adminDTO){
        super(adminDTO);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "isAdmin=" + isAdmin +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
