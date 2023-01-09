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