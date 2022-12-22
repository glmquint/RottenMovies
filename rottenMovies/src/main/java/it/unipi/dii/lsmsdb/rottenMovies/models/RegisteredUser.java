package it.unipi.dii.lsmsdb.rottenMovies.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.util.LinkedHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RegisteredUser {
    @JsonProperty("_id")
    private ObjectId id;

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    public ObjectId getId() {
        return id;
    }

    public void setId(Object id) {
        if(id instanceof LinkedHashMap<?,?>){
            LinkedHashMap link = (LinkedHashMap)id;
            this.id = new ObjectId(link.get("$oid").toString());
        }
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
