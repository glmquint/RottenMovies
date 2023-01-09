package it.unipi.dii.lsmsdb.rottenMovies.DAO.base;

import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.*;

public abstract class BaseNeo4jDAO implements AutoCloseable{
    protected final Driver driver;

    public BaseNeo4jDAO() {
        driver = GraphDatabase.driver(NEO4J_CONNECTION_STRING, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
    }

    @Override
    public void close() throws  RuntimeException{
        driver.close();
    }
}
