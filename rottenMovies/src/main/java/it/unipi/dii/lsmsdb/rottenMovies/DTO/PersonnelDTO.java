package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.util.ArrayList;

public class PersonnelDTO {
        private String primaryName;
        private String category;
        private String job;
        private ArrayList<String> characters;

    public PersonnelDTO() {
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public ArrayList<String> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<String> characters) {
        this.characters = characters;
    }
}
