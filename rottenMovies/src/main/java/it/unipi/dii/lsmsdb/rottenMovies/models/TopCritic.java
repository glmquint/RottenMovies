package it.unipi.dii.lsmsdb.rottenMovies.models;

public class TopCritic extends BaseUser {
    private int follower_count;

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    @Override
    public String toString() {

        return "TopCritic{" + '\n' +
                "_id " + getId().toString() + '\n' +
                "username " + getUsername() + '\n' +
                "password " + getPassword() + '\n' +
                "first_name " + getFirstName() + '\n' +
                "last_name " + getLastName() + '\n' +
                "follower_count " + getFollower_count() + '\n' +
                "last_3_review " + getLast3Reviews() + '\n' +
                "reviews " + getReviews() + '\n' +
                "}";
    }
}
