package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Personnel;

import java.util.ArrayList;

/**
 * <class>PersonnelDTO</class> is the container used to pass movies personnel information between
 * the service and presentation layer
 */
public class PersonnelDTO {
        private String primaryName;
        private String category;
        private String job;
        private ArrayList<String> characters;

    public PersonnelDTO() {
    }
    public PersonnelDTO(Personnel p) {
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
}
