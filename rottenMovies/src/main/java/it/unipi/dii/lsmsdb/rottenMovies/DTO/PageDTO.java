package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import java.util.List;
/**
 * <class>MovieReviewBombingDTO</class> is the container used to pass data in a paginated style between
 * the service and presentation layer
 */
public class PageDTO <T>{

    private List<T> entries;
    private int totalCount = 0;

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
        this.totalCount = entries.size();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "entries=" + entries +
                ", totalCount=" + totalCount +
                '}';
    }
}

