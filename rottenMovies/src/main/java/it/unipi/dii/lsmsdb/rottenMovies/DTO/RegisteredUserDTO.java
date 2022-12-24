package it.unipi.dii.lsmsdb.rottenMovies.DTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.RegisteredUser;
import org.bson.types.ObjectId;

public abstract class RegisteredUserDTO {
    protected ObjectId id;
    protected String username;
    protected String password;

    protected RegisteredUserDTO(){}
    protected RegisteredUserDTO(RegisteredUser u){
        this.id=u.getId();
        this.username=u.getPassword();
        this.password=u.getPassword();
    }
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}