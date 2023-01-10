package it.unipi.dii.lsmsdb.rottenMovies.models;

import it.unipi.dii.lsmsdb.rottenMovies.DTO.TopCriticDTO;

/**
 * <class>TopCritic</class> is the container in which data from the backend for TopCritic is mapped
 *                     RegisteredUser
 *                     /            \
 *                    /              \
 *                  Admin           BaseUser
 *                                  /       \
 *                                 /         \
 *                              User        TopCritic
 */
public class TopCritic extends BaseUser {
    private int follower_count;
    public TopCritic (){}
    public TopCritic (TopCriticDTO userdto){
        super(userdto);
        this.follower_count= userdto.getFollower_count();
    }
    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    @Override
    public String toString() {
        if (id == null){
            return "TopCritic{}";
        }
        return "TopCritic{" + '\n' +
                "_id " + getId().toString() + '\n' +
                "username " + getUsername() + '\n' +
                "password " + getPassword() + '\n' +
                "first_name " + getFirstName() + '\n' +
                "last_name " + getLastName() + '\n' +
                "follower_count " + getFollower_count() + '\n' +
                "last_3_review " + getLast3Reviews() + '\n' +
                "reviews " + getReviews() + '\n' +
                "isBanned " + isBanned() + '\n' +
                "}";
    }
}
