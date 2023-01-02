package it.unipi.dii.lsmsdb.rottenMovies.utils;

import com.mongodb.BasicDBObject;
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
}
