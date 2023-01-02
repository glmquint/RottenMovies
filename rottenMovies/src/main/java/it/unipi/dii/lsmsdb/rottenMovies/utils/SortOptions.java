package it.unipi.dii.lsmsdb.rottenMovies.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;

public class SortOptions {
    SortOptionsEnum type;
    int asc;

    public SortOptions(SortOptionsEnum type, int asc){
        this.type = type;
        this.asc = asc;
    }

    public Bson getSort() {
        String field = type.getField();
        if (!field.isEmpty() && !field.equals("no_sort")) {
            return new BasicDBObject(type.getField(), this.asc);
        } else{
            return null;
        }
    }
    public Bson getBsonAggregationSort() {
        String field = type.getField();
        if (!field.isEmpty() && (field.equals("user_rating") || field.equals("top_critic_rating"))) {
            if(asc==1)
                return Sorts.ascending(field);
            else
                return Sorts.descending(field);
        }
        else{
            return null;
        }
    }
}
