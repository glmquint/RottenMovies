
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
            {$in:db.user.find({ username: "Abbie Bernstein" }, { "reviews.primaryTitle": 1, "_id": 0 }).toArray()[0]['reviews'].map(x=>x['primaryTitle'])},
        }
    },
    {$unwind:"$genres"}, 
    {$group:{_id:"$genres", count:{$sum:1}}}, 
    {$sort:{count:-1}}, 
    {$limit: 10}
])

  ```
 ### Return the division of the user in terms of generation
```js
    
    db.user.aggregate([
        {
            $match:{"date_of_birth":{$exists:true}}
        },
        
        {
            $bucket:
            {
                groupBy: {$year:"$date_of_birth"},
                boundaries: [1970, 1975, 1980, 1985, 1990, 1995, 2000, 2005, 2010],
                output:
                {
                    "population": {$sum:1}   
                }
            }
        }
    ])

```
 ### Return the number of reviews made by year and by month. NOT WORKING WITH CURRENT DATABASE (string date)

```js 
db.movie.aggregate([
    {
        $match:{primaryTitle:"Black Panther"}
    },
    {$unwind:"$review"},
    {
        $group:
        {
            _id:{year:{$year:"$review.review_date"}, month:{$month:"$review.review_date"}},
            count:{$sum:1}
        }
    },
    {$sort:{_id:1}}
        
])
``` 
### Return the best decade for movies in terms of rating
```js
db.movie.aggregate([
    {
        $bucket:
        {
            groupBy: "$year",
            boundaries: [1900, 1910, 1920, 1930, 1940, 1950, 1960, 1970, 1980, 1990, 2000, 2010, 2020, 2030],
            default: "Without date",
            output:
                {
                    "topCritic":{$avg:"$top_critic_rating"},
                    "rate":{$avg:"$user_rating"}, 
                }
        }
    },
    {$sort:{topCritic:-1, rate:-1}}
    
])
```
### Return the best years in terms of ratings
```js
    db.movie.aggregate([
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
### Return the film with most reviews
```js
db.user.aggregate([
    {$unwind:"$reviews"}, 
    {$group:{_id:"$reviews.primaryTitle", count:{$sum:1}}}, 
    {$sort:{count:-1}}, 
    {$limit: 10}                         
])