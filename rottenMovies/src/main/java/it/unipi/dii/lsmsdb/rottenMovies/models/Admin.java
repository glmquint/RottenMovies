package it.unipi.dii.lsmsdb.rottenMovies.models;

import it.unipi.dii.lsmsdb.rottenMovies.DTO.AdminDTO;

public class Admin extends RegisteredUser {
    public Admin(AdminDTO adminDTO){
        super(adminDTO);
    }
}
