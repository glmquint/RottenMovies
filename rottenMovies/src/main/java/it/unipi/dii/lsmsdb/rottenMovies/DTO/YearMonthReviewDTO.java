package it.unipi.dii.lsmsdb.rottenMovies.DTO;

public class YearMonthReviewDTO {
    private int year;
    private int month;
    private int count;

    public YearMonthReviewDTO() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "YearMonthReviewDTO{" +
                "year=" + year +
                ", month=" + month +
                ", count=" + count +
                '}';
    }
}
