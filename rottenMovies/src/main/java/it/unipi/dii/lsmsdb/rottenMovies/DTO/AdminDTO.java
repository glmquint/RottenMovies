package it.unipi.dii.lsmsdb.rottenMovies.DTO;

import it.unipi.dii.lsmsdb.rottenMovies.models.Admin;

/**
 * <class>AdminDTO</class> is the container used to pass data regarding the admin between
 * the service and presentation layer
 */
public class AdminDTO extends RegisteredUserDTO{
    protected boolean isAdmin;
    public AdminDTO(){super();}

    public AdminDTO(Admin admin){
        super(admin);
        this.isAdmin = admin.isAdmin();
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
