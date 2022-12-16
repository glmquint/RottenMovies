package it.unipi.dii.lsmsdb.rottenMovies.models;

import java.util.Date;

public abstract class User extends RegistratedUser{
    private String firstName;
    private String lastName;
    private Date registrationDate;

    private boolean isTopCritic;

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

    public boolean isTopCritic() {
        return isTopCritic;
    }

    public void setTopCritic(boolean topCritic) {
        isTopCritic = topCritic;
    }
}
