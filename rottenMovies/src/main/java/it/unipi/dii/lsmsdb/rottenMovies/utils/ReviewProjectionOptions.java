package it.unipi.dii.lsmsdb.rottenMovies.utils;

import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;

import static it.unipi.dii.lsmsdb.rottenMovies.utils.Constants.REVIEWS_PER_PAGE;

public class ReviewProjectionOptions {
    ReviewProjectionOptionsEnum type;
    int offset;

    public ReviewProjectionOptions(ReviewProjectionOptionsEnum type, int offset){
        this.type = type;
        this.offset = offset;
    }

    public Bson getProjection(){
        Bson proj = null;
        switch (type){
            case HIDE:
                proj = Projections.exclude("review"); break;
            case SINGLE:
                proj = Projections.slice("review", offset, 1); break; // offset = comment index
            case SLICE:
                proj = Projections.slice("review", REVIEWS_PER_PAGE*offset, REVIEWS_PER_PAGE); break; // offset = page
            case ALL:
            default:
                break;
        }
        return proj;

    }
}
