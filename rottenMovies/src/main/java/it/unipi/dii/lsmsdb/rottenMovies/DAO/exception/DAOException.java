package it.unipi.dii.lsmsdb.rottenMovies.DAO.exception;

/**
 * @author Fabio
 * <class>DAOException</class> represent the exeption case in which
 * a particular method is called with the wrong DAO class
 */
public class DAOException extends Exception{

    public DAOException(){
        super();
    }
    public DAOException(String s){
        super(s);
    }
}
