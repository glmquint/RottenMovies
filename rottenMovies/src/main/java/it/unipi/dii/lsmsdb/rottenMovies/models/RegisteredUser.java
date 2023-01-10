package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.RegisteredUserDTO;
import org.bson.types.ObjectId;

import java.util.LinkedHashMap;

/**
 * <class>RegisteredUser</class> is the container in which data from the backend for RegisteredUser is mapped
 *                     RegisteredUser
 *                     /            \
 *                    /              \
 *                  Admin           BaseUser
 *                                  /       \
 *                                 /         \
 *                              User        TopCritic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RegisteredUser {
    @JsonProperty("_id")
    protected ObjectId id;
    @JsonProperty("username")
    protected String username;
    @JsonProperty("password")
    protected String password;

    protected RegisteredUser (){}
    protected RegisteredUser (RegisteredUserDTO userdto){
        this.id=userdto.getId();
        this.username=userdto.getUsername();
        this.password=userdto.getPassword();
    }
    public ObjectId getId() {
        return id;
    }

    public void setId(Object id) {
        if(id instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)id;
            this.id = new ObjectId(link.get("$oid").toString());
        }
        else if (id instanceof ObjectId){
            this.id= (ObjectId) id;
        }
    }

    //USARE SOLO PER TEST, DA RIMUOVERE
    public void setIdString(String id){
        this.id = new ObjectId(id);
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
