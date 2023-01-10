db.movie.aggregate([
    {$group:
        {
            _id: "$production_company",
            topCritic:{$avg:"$top_critic_rating"},
            rate:{$avg:"$user_rating"},
            count:{$sum:1}
        }
    }, 
    {$match:{count:{$gte:<<i>>}}},
    {$sort:{topCritic:-1, rate:-1}},
    {$limit:<<j>>}
])