db.movie.aggregate([
    {$match:
        {"review.critic_name":{$eq:<<"name">>}}  
    },
    {$unwind:"$genres"}, 
    {$group:{_id:"$genres", count:{$sum:1}}}, 
    {$sort:{count:-1}}, 
    {$limit: <<n>>}
])