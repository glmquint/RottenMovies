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
    for i, item in enumerate(collection.find()):
        filter = { 'username': item['username']}
        result = hashlib.md5(item["username"].encode()).hexdigest()        
        newvalues = { "$set": { 'password': result } }
        collection.update_one(filter, newvalues)
        print(f"{i/total:%}\r", end='')
