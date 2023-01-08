
db.movie.find().forEach(
    x => {
        print(x.primaryTitle);
        x.review = JSON.parse(
            x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll(/\\x\d{2}/g, "")
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll("\\x", "x")
        );
        x.personnel = JSON.parse(
            x.personnel.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", '\'')
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('"[\\"', '["')
                .replaceAll('\']"', '"]')
                .replaceAll('\\"]"', '"]')
                .replaceAll(/(\[[^[:]*)\\", \\"([^]:]*\])/g, '$1", "$2')
                .replaceAll(/(\[[^[:]*)\', \\"([^]:]*\])/g, '$1", "$2')
                .replaceAll(/(\[[^[:]*)\\", \'([^]:]*\])/g, '$1", "$2')
        );
        x.genres = JSON.parse(
            x.genres = x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
        );
        db.movie.updateOne(
            {"_id": x._id}, 
            {$set: 
                {
                    "review": x.review,
                    "personnel": x.personnel,
                    "genres": x.genres,
                    "runtimeMinutes":parseInt(x.runtimeMinutes),
                    "year":parseInt(x.year), 
                    "tomatometer_rating":parseFloat(x.tomatometer_rating), 
                    "audience_rating":parseFloat(x.audience_rating), 
                    "audience_count":parseFloat(x.audience_count), 
                    "tomatometer_fresh_critics_count":parseInt(x.tomatometer_fresh_critics_count), 
                    "tomatometer_rotten_critics_count":parseInt(x.tomatometer_rotten_critics_count)
                }
            }
        );
    }
);
