## review
```js
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

```js
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

```js
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

```js
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

```js
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

```js
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

```js
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

```js
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

```js
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

```js
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

```js
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
### create rating movie fields 

```js
total = db.movie.find().count();
i = 0;

db.movie.find().forEach(
    x => {
        print(x.primaryTitle);
        y=x.review;
        top_critic_fresh_count=0;
        top_critic_rotten_count=0;
        user_fresh_count=0;
        user_rotten_count=0;
        y.forEach(rev => {
            if(rev.top_critic){
                if(rev.review_type=="Fresh"){
                    top_critic_fresh_count++;
                }
                else{ 
                    top_critic_rotten_count++;
                }
            }
            else {
                if(rev.review_type=="Fresh"){
                    user_fresh_count++;
                }
                else {
                    user_rotten_count++;
                }
            }
            top_critic_status="";
            top_critic_rating=0;
            if(top_critic_fresh_count!=0 || top_critic_rotten_count!=0){
                top_critic_rating=~~(((top_critic_fresh_count/(top_critic_rotten_count+top_critic_fresh_count))*100)+0.5);
                if(top_critic_rating>=60){
                    top_critic_status="Fresh";
                    if(top_critic_rating>=75 && 
                        (top_critic_fresh_count+top_critic_rotten_count+user_fresh_count+user_rotten_count>=80)&&
                        (top_critic_fresh_count+top_critic_rotten_count>=5)){
                            top_critic_status="Certified Fresh";
                    }
                }
                else{
                    top_critic_status="Rotten";
                }
            }
            user_status="";
            user_rating=0;
            if(user_fresh_count!=0 || user_rotten_count!=0){
                user_rating=~~(((user_fresh_count/(user_fresh_count+user_rotten_count))*100)+0.5);
                if(user_rating>=60){
                    user_status="Upright";
                }
                else{
                    user_status="Spilled";
                }
            }
            
        })
               
        db.movie.updateOne(
            {"primaryTitle": x.primaryTitle},
            {$set: 
                {"top_critic_fresh_count": top_critic_fresh_count,
                "top_critic_rotten_count": top_critic_rotten_count,
                "user_fresh_count": user_fresh_count,
                "user_rotten_count": user_rotten_count,
                "tomatometer_rating": top_critic_rating,
                "tomatometer_status": top_critic_status,
                "audience_status": user_status,
                "audience_rating": user_rating
                }
            }
        )
        db.movie.updateOne(
            {"primaryTitle": x.primaryTitle},
            {$rename:{'tomatometer_status':'top_critic_status',
                    'tomatometer_rating':'top_critic_rating',
                    'audience_status':'user_status',
                    'audience_rating':'user_rating'}
            }
        )
        db.movie.updateOne(
            {"primaryTitle": x.primaryTitle},
            {$unset: {audience_count:"",tomatometer_fresh_critics_count:"",tomatometer_rotten_critics_count:""}}

        )
        print(100*i++/total);
    }

);

```
#### parse type strings to floats and integers
```js
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
```js
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

#### get all reviewed movies for each user
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

#### split first_name and last name from username
```js
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
```js 
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
```js 
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

```js 
db.runCommand(
{ distinct: "movie", key: "review.critic_name" }).values.forEach(
    (x) => {
        arr = []
        db.movie.aggregate(
            [
                { $project: 
                    {
                        index: { $indexOfArray: ["$review.critic_name", x]}
                    }},
                {$match:{index:{$gt:-1}}}
            ]
        ).forEach(
            y => {
                arr.push(db.movie.aggregate([
                    {
                        $project:
                        {
                            movie_id: 1,
                            top_critic: {
                                $arrayElemAt: ["$review.top_critic", y.index]
                            },
                            critic_name: {
                                $arrayElemAt: ["$review.critic_name", y.index]
                            },
                            review_type: {
                                $arrayElemAt: ["$review.review_type", y.index]
                            },
                            review_score: {
                                $arrayElemAt: ["$review.review_score", y.index]
                            },
                            review_date: {
                                $arrayElemAt: ["$review.review_date", y.index]
                            },
                            review_content: {
                                $arrayElemAt: ["$review.review_content", y.index]
                            }
                        }
                    },
                    {
                        $match:{_id:{$eq:y._id}}
                    }
                ]).toArray()[0])
            })
        print(x, arr)
    }   
)
                
```

### user creation
```js
total = db.runCommand({ distinct: "movie", key: "review.critic_name", query: {"review.critic_name":{$ne:null}}}).values.length
i = 0;
db.runCommand(
{ distinct: "movie", key: "review.critic_name", query: {"review.critic_name":{$ne:null}}}).values.forEach(
    (x) => {
        review_arr = []
        movie_arr = []
        is_top = false
        db.movie.aggregate(
            [
                { $project: 
                    {
                        index: { $indexOfArray: ["$review.critic_name", x]},
                        primaryTitle: 1
                    }},
                {$match:{index:{$gt:-1}}}
            ]
        ).forEach(
            y => {
                tmp = db.movie.aggregate([
                    {
                        $project:
                        {
                            top_critic: {
                                $arrayElemAt: ["$review.top_critic", y.index]
                            },
                            primaryTitle: y.primaryTitle,
                            review_type: {
                                $arrayElemAt: ["$review.review_type", y.index]
                            },
                            review_score: {
                                $arrayElemAt: ["$review.review_score", y.index]
                            },
                            review_date: {
                                $arrayElemAt: ["$review.review_date", y.index]
                            },
                            review_content: {
                                $arrayElemAt: ["$review.review_content", y.index]
                            }
                        }
                    },
                    {
                        $match:{_id:{$eq:y._id}}
                    }
                ]).toArray()[0];
                is_top |= tmp.top_critic;
                review_arr.push(tmp)
                //movie_arr.push(tmp._id)
                movie_arr.push({"movie_id": tmp._id, "primaryTitle": y.primaryTitle, "review_index": y.index})
            })

        name_parts = x.split(/\s/)
        first_name = name_parts.splice(0, 1)[0]
        last_name = name_parts.join(' ')

        print(100*i++/total, x, is_top)
        //print(first_name, ':', last_name)
        //print(review_arr)
        //print(movie_arr)
        db.user.insertOne(
            {
                "username": x,
                "password": "",
                "first_name": first_name,
                "last_name": last_name,
                "registration_date": new Date("2000-01-01"),
                "last_3_reviews": review_arr,
                "reviews" : movie_arr
            }
        );
        if (!is_top){
            db.user.updateOne(
                {"username": x},
                {$set: 
                    {"date_of_birth": new Date("1970-07-20")}
                }
            )
        }
        print("================================")
    }   
)
                
```
#### modify users date_of_birth  
```js
db.user.updateMany({"date_of_birth":{$exists:true}}, {$set: {"date_of_birth": new Date("1970-07-20")}} )
```
#### an imposter (find the error)

```js
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

```js
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

```js
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