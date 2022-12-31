
    ### Return the best year between a certain set in terms of movies produces

```js
    
    
    db.movie.aggregate([
        {$match:{year:{$gt:1940, $lt:1950}}},
        {$group:
            {
            _id: "$year",
            topCritic:{$avg:"$top_critic_rating"},
            rate:{$avg:"$user_rating"},
            count:{$sum:1}
            }
        },
        {$match:{count:{$gte:20}}},
        {$limit: 10}, 
        {$sort:{topCritic:-1, rate:-1}}
    ])

```

 ### Return the most succesfull production houses who have made at least 10 movies

```js
db.movie.aggregate([
    {$group:
        {
            _id: "$production_company",
            topCritic:{$avg:"$top_critic_rating"},
            rate:{$avg:"$user_rating"},
            count:{$sum:1}
        }
    }, 
    {$match:{count:{$gte:10}}},
    {$sort:{topCritic:-1, rate:-1}},
    {$limit:10}
])

 ```

 ### Return the most reviewd genres by a signle user
```js
db.movie.aggregate([
    {$match:
        {primaryTitle:
            {$in:db.user.find({ username: "Abbie Bernstein" }, { "reviews.primaryTitle": 1, "_id": 0 }).toArray()[0]['reviews'].map(x=>x['primaryTitle'])}
        }
    },
    {$unwind:"$genres"}, 
    {$group:{_id:"$genres", count:{$sum:1}}}, 
    {$sort:{count:-1}}, 
    {$limit: 10}
])

  ```