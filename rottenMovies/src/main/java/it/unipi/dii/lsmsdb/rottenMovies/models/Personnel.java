package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PersonnelDTO;


import java.util.ArrayList;

/**
 * <class>Personnel</class> is the container in which data from the backend for Personnel is mapped
 */
public class Personnel {
    @JsonProperty("primaryName")
    private String primaryName;
    @JsonProperty("category")
    private String category;
    @JsonProperty("job")
    private String job;
    @JsonProperty("characters")
    private ArrayList<String> characters;

    public Personnel() {
    }

    public Personnel(PersonnelDTO p) {
        this.primaryName=p.getPrimaryName();
        this.category=p.getCategory();
        this.job=p.getJob();
        this.characters=p.getCharacters();
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

    @Override
    public String toString() {
        return "Personnel{" +
                "primaryName='" + primaryName + '\'' +
                ", category='" + category + '\'' +
                ", job='" + job + '\'' +
                ", characters=" + characters +
                '}';
    }
}
