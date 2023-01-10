db.movie.aggregate([
    {
        $unwind:"$genres"
    },
    {$group:
        {
            _id: "$genres",
            topCriticRating:{$avg:"$top_critic_rating"},
            userRating:{$avg:"$user_rating"},
            count:{$sum:1}
        }
    }, 
    {$match:{count:{$gte:<<i>>}}},
    {$sort:{[topCriticRating/userRating]:-1}},
    {$limit:<<j>>}
])