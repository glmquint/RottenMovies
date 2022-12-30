package it.unipi.dii.lsmsdb.rottenMovies.utils;

import com.mongodb.BasicDBObject;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;

public enum sortOptions {
        ALPHABET("primaryTitle"),
        DATE("year"),
        USER_RATING("user_rating"),
        TOP_CRITIC_RATING("top_critic_rating");

        private String field;

        sortOptions(String envField) {
            this.field = envField;
        }

        public Bson getField(int asc) {
            return new BasicDBObject(field,asc);
        }
}
