package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.exception.DAOException;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.UserLeaderboardDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

/**
 * <class>AdminMongoDB_DAO</class> is responsible for the admin-field operation in
 * the MongoDB
 */
public class AdminMongoDB_DAO extends BaseMongoDAO implements AdminDAO {
    /**
     * <method>userPopulationByGeneration</method> calculate the population distribution in buckets
     * @param offset is the number of years on which the buckets are created
     * @return an Arraylist of PopulationByGenerationDTO
     * @throws DAOException
     */
    @Override
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration(int offset) throws DAOException {
        MongoCollection<Document> collectionUser = getUserCollection();
        BucketOptions opt = new BucketOptions();
        ArrayList<Integer> buck=new ArrayList<>();
        opt.output(new BsonField("population",new Document("$sum",1)));
        int bucketYear=1970; // start year
        buck.add(bucketYear);
        while(bucketYear<=2010){ // add the offset until you reach 2010
            bucketYear=(bucketYear+offset);
            buck.add(bucketYear);
        }
        AggregateIterable<Document> aggregateResult = collectionUser.aggregate(
                Arrays.asList(
                        Aggregates.match(exists("date_of_birth")),
                        Aggregates.bucket(new Document("$year","$date_of_birth"),buck,opt)
                )
        );
        ArrayList<PopulationByGenerationDTO> resultSet = new ArrayList<>();
        PopulationByGenerationDTO populationByGenerationDTO;
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            populationByGenerationDTO = new PopulationByGenerationDTO();
            populationByGenerationDTO.setYear(doc.getInteger("_id"));
            populationByGenerationDTO.setCount(doc.getInteger("population"));
            resultSet.add(populationByGenerationDTO);
        }
        return resultSet;
    }
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration() throws DAOException{
        return userPopulationByGeneration(5);
    }

    /**
     * <method>changeUserStatus</method> change the status of a user from unbanned to banned and vice-versa
     * @param userId is the id of the user targeted by the operation
     * @param ban indicates if the operation is a ban one (true) or an unban one (false)
     * @return true the target user document has been update
     * @throws DAOException
     */
    @Override
    public boolean changeUserStatus(ObjectId userId,boolean ban) throws DAOException {
        MongoCollection<Document>  collection = getUserCollection();
        Bson usrFilter = eq("_id",userId);
        Bson update;
        UpdateResult result;
        if(ban){
            update=Updates.set("isBanned",true);
        }
        else {
            update = Updates.unset("isBanned");
        }
        result=collection.updateOne(usrFilter,update);
        return (result.getModifiedCount()!=0);
    }

    public ArrayList<UserLeaderboardDTO> getMostReviewUser() throws DAOException{
        throw new DAOException("requested a query for the Neo4j in the MongoDB connection");
    }


    public ArrayList<UserLeaderboardDTO> getMostFollowedCritic() throws DAOException {
        throw new DAOException("requested a query for the Neo4j in the MongoDB connection");
    }


}

