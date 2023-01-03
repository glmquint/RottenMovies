package it.unipi.dii.lsmsdb.rottenMovies.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.base.BaseMongoDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DAO.interfaces.AdminDAO;
import it.unipi.dii.lsmsdb.rottenMovies.DTO.GenresLikeDTO;
import it.unipi.dii.lsmsdb.rottenMovies.utils.Constants;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

public class AdminMongoDB_DAO extends BaseMongoDAO implements AdminDAO {
    @Override
    public ArrayList<Integer> userPopulationByGeneration(int start, int offset,int index) {
        MongoCollection<Document> collectionUser = getUserCollection();
        BucketOptions opt = new BucketOptions();
        ArrayList<Integer> buck=new ArrayList<>();
        opt.output(new BsonField("population",new Document("$sum",1)));
        for(int i=0; i<=index; i++){
            buck.add(start+(offset*i));
        }
        AggregateIterable<Document> aggregateResult = collectionUser.aggregate(
                Arrays.asList(
                        Aggregates.match(exists("date_of_birth")),
                        Aggregates.bucket(new Document("$year","$date_of_birth"),buck,opt)
                )
        );
        ArrayList<Integer> resultSet = new ArrayList<>();
        MongoCursor<Document> cursor = aggregateResult.iterator();
        while (cursor.hasNext()){
            Document doc = cursor.next();
            resultSet.add(doc.getInteger("population"));
        }
        return resultSet;
    }
    public ArrayList<Integer> userPopulationByGeneration(){
        return userPopulationByGeneration(1970,5,8);
    }
}
