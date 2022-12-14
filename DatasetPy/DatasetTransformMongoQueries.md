## review
```py
db.test.find().limit(5).forEach(
    x => {
        print(
            x.review.replaceAll('"\'', '"')
                .replaceAll('\'"', '"')
                .replaceAll('"false"', 'false')
                .replaceAll('"true"', 'true')
                .replaceAll('"None"', 'null')
                .replaceAll("##single-quote##", "\'")
                .replaceAll("##double-quote##", '\\"')
        )
    }
)
```

### review JSON

```py
db.test.find().limit(5).forEach(
    x => {
        print(
            JSON.parse(
                x.review.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"false"', 'false')
                    .replaceAll('"true"', 'true')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
            )
        )
    }
)
```

### review update query

```py
db.test.find().limit(5).forEach(
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
        db.test.updateOne(
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
db.test.find().limit(5).forEach(
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
db.test.find().limit(5).forEach(
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
db.test.find().limit(5).forEach(
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
        db.test.updateOne(
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
db.test.find().limit(5).forEach(
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
db.test.find().limit(5).forEach(
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
db.test.find().limit(5).forEach(
    x => {
        x.genres = JSON.parse(
            x.genres.replaceAll('"\'', '"')
                    .replaceAll('\'"', '"')
                    .replaceAll('"None"', 'null')
                    .replaceAll("##single-quote##", "\'")
                    .replaceAll("##double-quote##", '\\"')
        );
        db.test.updateOne(
            {"_id": x._id}, 
            {$set: 
                {"genres": x.genres}
            }
        )
    }
)
```

---

### test final

```py
db.test.find().forEach(
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

#### this is final for entire dataset (this is the one)

```py
db.test.find().forEach(
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
        db.test.updateOne(
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

(remember to drop `tconst` as we use `_id` as the final index in mongo)

```
SELECT test.primary_title
FROM test
WHERE test.review.critic_name = "$x"
```
translates to

`db.runCommand({ distinct: "test", key: "review.critic_name" }).values.forEach((x) => {print(x, db.test.find({ "review.critic_name": x }, { primaryTitle: 1, _id:0 }).count() )})`

checkpoint
```
db.runCommand({ distinct: "test", key: "review.critic_name" }).values.forEach(x => {
    db.test.find(
        { "review.critic_name": x }, { primaryTitle: 1, _id:0 }
    ).forEach(y => {
        db.test.find(
            {primaryTitle: y.primaryTitle}, 
            {"review.critic_name": 1} 
        ).review.forEach(z =>{
            print(z)
        }
        )
    }) 
})
```

#### an imposter (find the error)

```py
db.test.find().forEach(
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
        db.test.updateOne(
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

### another test

```py
db.test.find().forEach(
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
db.test.find().forEach(
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
        db.test.updateOne(
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