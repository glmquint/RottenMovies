package it.unipi.dii.lsmsdb.rottenMovies.utils;

import com.mongodb.BasicDBObject;
import org.bson.conversions.Bson;

public enum SortOptionsEnum {
        ALPHABET("primaryTitle"),
        DATE("year"),
        USER_RATING("user_rating"),
        TOP_CRITIC_RATING("top_critic_rating"),
        NO_SORT("no_sort");

        private String field;

        SortOptionsEnum(String envField) {
            this.field = envField;
        }
        public String getField() {
                return this.field;
        }
}
