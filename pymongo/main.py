import hashlib
#from pprint import pprint as print
from pymongo import MongoClient

def get_database():
   CONNECTION_STRING = "mongodb://localhost:27017"
   client = MongoClient(CONNECTION_STRING)
   return client['rottenMovies']
  
if __name__ == "__main__":   
    dbname = get_database()
    collection = dbname['user']
    total = collection.count_documents({})
    for i, user in enumerate(collection.find()):
        all_reviews = user['last_3_reviews']
        sorted_list = sorted(all_reviews, key=lambda t: t['review_date'])[-3:]

        hashed = hashlib.md5(user["username"].encode()).hexdigest()        

        newvalues = { "$set": { 'password': hashed, 'last_3_reviews': sorted_list } }
        filter = { 'username': user['username']}
        collection.update_one(filter, newvalues)
        print(f"{i/total:%}\r", end='')
    print()
