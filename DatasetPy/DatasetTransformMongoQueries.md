## review
```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll(/\\x\d{2}/g, "")
                .replaceAll(/"([0-9-]{10}T[0-9:.+]{18})"/g, "$1")
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll("\\x", "x")
        )
    }
)
```

### review JSON

```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            JSON.parse(
                x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll(/\\x\d{2}/g, "")
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll("\\x", "x")
            )
        )
    }
)
```

### review update query

```py
db.movie.find().limit(5).forEach(
    x => {
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
        db.movie.updateOne(
            {"_id": x._id}, 
            {$set: 
                {"review": x.review}
            }
        )
    }
)
```

---

## personnel

```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            x.personnel.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('\']"', '"]')
        )
    }
)
```

### personnel JSON

```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            JSON.parse(
                x.personnel.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
                    .replaceAll('"[\'', '["')
                    .replaceAll('\']"', '"]')
            )
        )
    }
)
```

### personnel update query

```py
db.movie.find().limit(5).forEach(
    x => {
        x.personnel = JSON.parse(
            x.personnel.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('\']"', '"]')
        );
        db.movie.updateOne(
            {"_id": x._id}, 
            {$set: 
                {"personnel": x.personnel}
            }
        )
    }
)
```

---

## genre

```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            x.genres.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
        )
    }
)
```

### genre JSON

```py
db.movie.find().limit(5).forEach(
    x => {
        print(
            JSON.parse(
                x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
            )
        )
    }
)
```

### genre update query

```py
db.movie.find().limit(5).forEach(
    x => {
        x.genres = JSON.parse(
            x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
        );
        db.movie.updateOne(
            {"_id": x._id}, 
            {$set: 
                {"genres": x.genres}
            }
        )
    }
)
```

---

## movie final

```py
db.movie.find().forEach(
    x => {
        x.review = x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll(/\\x\d{2}/g, "")
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll("\\x", "x");
        
        x.review = JSON.parse(x.review);

        x.personnel = x.personnel.replaceAll('"\'', '"')
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
                .replaceAll(/(\[[^[:]*)\\", \'([^]:]*\])/g, '$1", "$2');
        
        x.personnel = JSON.parse(x.personnel);
        x.genres = x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"');
        
        x.genres = JSON.parse(x.genres);
    }
);
```

### this is final for entire dataset (this is the real one)

```py
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
```

#### parse type strings to floats and integers
```py
db.movie.find().forEach(
    (x)=>{
        db.movie.updateOne(
            {"_id":x._id},
            {"$set":{
                "runtimeMinutes":parseInt(x.runtimeMinutes),
                "year":parseInt(x.year), 
                "tomatometer_rating":parseFloat(x.tomatometer_rating), 
                "audience_rating":parseFloat(x.audience_rating), 
                "audience_count":parseFloat(x.audience_count), 
                "tomatometer_fresh_critics_count":parseInt(x.tomatometer_fresh_critics_count), 
                "tomatometer_rotten_critics_count":parseInt(x.tomatometer_rotten_critics_count)
                }
            }
        )
    }
) ;

# or alternatively:

db.movie.updateMany({}, 
    {"$set":{
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
```

#### review_date parsing
```py
db.movie.find().forEach(
    m => {
        print(m.primaryTitle);
        m.review.forEach(
            r => {
                r.review_date = new Date(r.review_date);
                db.movie.updateOne(
                    {
                        _id: m._id, 
                        "review.critic_name": r.critic_name
                    },
                    {
                        $set: {"review.$.review_date": r.review_date}
                    }
                );
            }
        );
    }
)
```

`r.review_date = new Date(r.review_date + "T00:00:00Z");`

---

```sql
SELECT COUNT(movie.primary_title)
FROM movie
WHERE movie.review.critic_name = "$x"
```
translates to

```
db.runCommand({ distinct: "movie", key: "review.critic_name" }).values.forEach(
    (x) => {
        print(x, 
            db.movie.find(
                { "review.critic_name": x }, 
                { primaryTitle: 1, _id:0 }
            ).count() 
        )
    }
)
```

```sql
SELECT movie._id
FROM movie
WHERE movie.review.critic_name = "$x"
```
translates to

```
db.runCommand({ distinct: "movie", key: "review.critic_name" }).values.forEach(
    (x) => {
        print(x, 
            db.movie.find(
                { "review.critic_name": x }, 
                { _id:1 }
            )
        )
    }
)
```

checkpoint
```
db.runCommand({ distinct: "movie", key: "review.critic_name" }).values.forEach(x => {
    db.movie.find(
        { "review.critic_name": x }, { primaryTitle: 1, _id:0 }
    ).forEach(y => {
        db.movie.find(
            {primaryTitle: y.primaryTitle}, 
            {"review.critic_name": 1} 
        ).review.forEach(z =>{
            print(z)
        }
        )
    }) 
})
```

```
i = 0;
total = db.movie.find().count();
db.movie.find().forEach(
    x => {
        i++;
        print(100 * i/total)
})
```

```py
i = 0;
db.runCommand({distinct: "movie", key: "review.critic_name"}).values.forEach(
    x => {
        name_parts = x.split(/[^.]\s/)
        first_name = name_parts.splice(0, 1)[0]
        last_name = name_parts.join(' ')
        print(100*i++/total, first_name, ':', last_name)
    }
)
```

#### for each user get their review for each reviewed movie
```py 
db.runCommand(
{ distinct: "movie", key: "review.critic_name" }).values.forEach(
    (x) => {
        print(x)
        db.movie.aggregate(
            [
                { $project: 
                    {
                        index: { $indexOfArray: ["$review.critic_name", x]}
                    }},
                {$match:{index:{$gt:-1}}}
            ]
        ).forEach(y => {
            db.movie.aggregate([
                {
                    $project:
                    {
                        review_content: {
                            $arrayElemAt: ["$review", y.index]
                        }
                    }
                },
                {
                    $match:{_id:{$eq:y._id}}
                }
            ])
        })
    }   
)
                
```

next step: create new new user in `forEach(x)`, then append found aggregated review to list of reviews for that user

#### get if user is top_critic from all its reviews
```py 
i = 0;
db.runCommand(
{ distinct: "movie", key: "review.critic_name" }).values.forEach(
    (x) => {
        is_top = false
        db.movie.aggregate(
            [
                { $project: 
                    {
                        index: { $indexOfArray: ["$review.critic_name", x]}
                    }},
                {$match:{index:{$gt:-1}}}
            ]
        ).forEach(y => {
            is_top |= db.movie.aggregate([
                {
                    $project:
                    {
                        is_top_critic: {
                            $arrayElemAt: ["$review.top_critic", y.index]
                        }
                    }
                },
                {
                    $match:{_id:{$eq:y._id}}
                }
            ]).toArray()[0].is_top_critic
        })
        print(100*i++/total, x, is_top)
    }   
)
                
```

#### an imposter (find the error)

```py
db.movie.find().forEach(
    x => {
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
                .replaceAll("##single-quote##", '\\"')
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('\']"', '"]')
                .replaceAll("', \"", '", "')
                .replaceAll("\", '", '", "')
                .replaceAll("', '", '", "')
        );
        x.genres = JSON.parse(
            x.genres.replaceAll('"\'', '"')
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
                    "genres": x.genres
                }
            }
        );
    }
);
```

### another movie

```py
db.movie.find().forEach(
    x => {
        print(x.review);
        x.review = JSON.parse(
            x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll(/\\x\d{2}/g, "")
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
        );
        print(x.personnel);
        x.personnel = JSON.parse(
            x.personnel.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('\']"', '"]')
        );
        print(x.genres);
        x.genres = JSON.parse(
            x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
        );
    }
);
```

## final script (this works on head)

```py
db.movie.find().forEach(
    x => {
        x.review = JSON.parse(
            x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
        );
        x.personnel = JSON.parse(
            x.personnel.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
                .replaceAll('"[\'', '["')
                .replaceAll('\']"', '"]')
        );
        x.genres = JSON.parse(
            x.genres.replaceAll('"\'', '"')
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
                    "genres": x.genres
                }
            }
        );
    }
);
```
