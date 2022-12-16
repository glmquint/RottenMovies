package it.unipi.dii.lsmsdb.rottenMovies.models;

public abstract class RegistratedUser {
    private String username;
    private String password; //potrebbe non servire averla nella classe

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
