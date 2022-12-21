package it.unipi.dii.lsmsdb.rottenMovies.DAO.base;

import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public abstract class BaseNeo4jDAO implements AutoCloseable{
    private final Driver driver;
    private static final String user = "neo4j";
    private static final String password = "password";

    public BaseNeo4jDAO() {
        driver = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws  RuntimeException{
        driver.close();
    }
}
