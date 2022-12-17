package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Personnel {
    @SerializedName("primaryName")
    private String primaryName;
    @SerializedName("category")
    private String category;
    @SerializedName("job")
    private String job;
    @SerializedName("characters")
    private ArrayList<String> characters;

    public Personnel() {
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
