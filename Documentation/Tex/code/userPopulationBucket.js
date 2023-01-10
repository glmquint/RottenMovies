db.user.aggregate([
    {
        $match:{"date_of_birth":{$exists:true}}
    },
    
    {
        $bucket:
        {
            groupBy: {$year:"$date_of_birth"},
            boundaries: [<<values>>],
            output:
            {
                "population": {$sum:1}   
            }
        }
    }
])