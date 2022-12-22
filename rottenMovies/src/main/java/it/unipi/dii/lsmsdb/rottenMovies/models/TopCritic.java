package it.unipi.dii.lsmsdb.rottenMovies.models;

public class TopCritic extends BaseUser {
    private int follower_count;

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }
}
