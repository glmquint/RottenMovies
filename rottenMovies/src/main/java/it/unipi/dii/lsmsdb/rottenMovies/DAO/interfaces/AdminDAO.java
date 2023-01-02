package it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces;

import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;

import java.util.ArrayList;

public interface AdminDAO extends AutoCloseable{
    ArrayList<Integer> userPopulationByGeneration (int start,int offset,int index);
}
