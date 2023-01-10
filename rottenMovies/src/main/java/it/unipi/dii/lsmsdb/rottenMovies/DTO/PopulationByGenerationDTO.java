package it.unipi.dii.lsmsdb.rottenMovies.DTO;

/**
 * <class>PopulationByGenerationDTO</class> is the container used to pass information for the age distribution between
 * the service and presentation layer
 */

public class PopulationByGenerationDTO {
    private int year;
    private int count;

    public PopulationByGenerationDTO() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
