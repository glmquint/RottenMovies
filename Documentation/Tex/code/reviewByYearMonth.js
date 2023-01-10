db.movie.aggregate([
    {
        $match:{id:<<"id">>}
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