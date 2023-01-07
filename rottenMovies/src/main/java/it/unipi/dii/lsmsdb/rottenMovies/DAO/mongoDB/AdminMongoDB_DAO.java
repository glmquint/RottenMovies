package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.PopulationByGenerationDTO;
import it.unipi.dii.lsmsdb.rottenMovies.models.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

public class AdminMongoDB_DAO extends BaseMongoDAO implements AdminDAO {
    @Override
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration(int offset) {
        MongoCollection<Document> collectionUser = getUserCollection();
        BucketOptions opt = new BucketOptions();
        ArrayList<Integer> buck=new ArrayList<>();
        opt.output(new BsonField("population",new Document("$sum",1)));
        // parti dal 1970
        // aggiungi offset
        // ripeti fino al 2010
        int bucketYear=1970;
        buck.add(bucketYear);
        while(bucketYear<=2010){
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
    public ArrayList<PopulationByGenerationDTO> userPopulationByGeneration(){
        return userPopulationByGeneration(5);
    }

    @Override
    public boolean changeUserStatus(ObjectId userId,boolean ban) {
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

}

