package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.TopCritic;

public class TopCriticDTO extends BaseUserDTO{
    private int follower_count;
    public TopCriticDTO(){super();}
    public TopCriticDTO(TopCritic user){
        super(user);
        this.follower_count=user.getFollower_count();
    }
    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }


}
