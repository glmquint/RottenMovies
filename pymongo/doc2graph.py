from pymongo import MongoClient
from neo4j import GraphDatabase
from random import randint, choice

def get_database():
   CONNECTION_STRING = "mongodb://localhost:27017"
   client = MongoClient(CONNECTION_STRING)
   return client['rottenMovies']

class Neo4jGraph:

    def __init__(self, uri, user, password):
        self.driver = GraphDatabase.driver(uri, auth=(user, password), database="rottenmoviesgraphdb")

    def close(self):
        self.driver.close()

    def addUser(self, uid, name, isTop):
        with self.driver.session() as session:
            if isTop:
                result = session.execute_write(self._addTopCritic, uid, name) 
            else:
                result = session.execute_write(self._addUser, uid, name) 

    def addMovie(self, mid, title):
        with self.driver.session() as session:
            result = session.execute_write(self._addMovie, mid, title) 

    def addReview(self, name, mid, freshness, content, date):
        with self.driver.session() as session:
            result = session.execute_write(self._addReview, name, mid, freshness, content, date)

    def addFollow(self, uid, cid):
        with self.driver.session() as session:
            result = session.execute_write(self._addFollow, uid, cid)

    @staticmethod
    def _addUser(tx, uid, name):
        query = "CREATE (n:User{id:\"" + str(uid).replace('"', '\\"') + "\", name:\"" + name.replace('"', '\\"') + "\"})"
        #print(query)
        result = tx.run(query)

    @staticmethod
    def _addTopCritic(tx, cid, name):
        query = "CREATE(m:TopCritic{id:\"" + str(cid).replace('"', '\\"') + "\", name:\"" + name.replace('"', '\\"') + "\"})"
        #print(query)
        result = tx.run(query)

    @staticmethod
    def _addMovie(tx, mid, title):
        query = "CREATE(o:Movie{id:\"" + str(mid).replace('"', '\\"') + "\", title:\"" + title.replace('"', '\\"') + "\"})"
        #print(query)
        result = tx.run(query)

    @staticmethod
    def _addReview(tx, name, mid, freshness, content, date): # date in format YYYY-mm-dd, freshness in [TRUE, FALSE]
        query = "MATCH(n{name:\"" + str(name).replace('"', '\\"') + "\"}), (m:Movie{id:\"" + str(mid).replace('"', '\\"') + "\"}) CREATE (n)-[r:REVIEWED{freshness:" + freshness + ", date:date('" + date + "'), content:\"" + content.replace('"', '\\"') + "\"}]->(m)"
        #print(query)
        result = tx.run(query)

    @staticmethod
    def _addFollow(tx, uid, cid):
        query = "MATCH(n:User{id:\"" + str(uid).replace('"', '\\"') + "\"}), (m:TopCritic{id:\"" + str(cid).replace('"', '\\"') + "\"}) CREATE (n)-[r:FOLLOWS]->(m)"
        #print(query)
        result = tx.run(query)

if __name__ == "__main__":
    

    dbname = get_database()
    collection = dbname['user']
    total = collection.count_documents({})
    print(f"user {total = }")
    graphDB = Neo4jGraph("bolt://localhost:7687", "neo4j", "password")

    """
    for i, user in enumerate(list(collection.find({}, {"_id":1, "username":1, "date_of_birth":1}))):
        graphDB.addUser(user['_id'], user['username'], 'date_of_birth' not in user)
        if not i%100:
            print(f"{(i+1)/total:%}\r", end='')
    """

    collection = dbname['movie']
    total = collection.count_documents({})
    print(f"\nmovie {total = }")
    for i, movie in enumerate(list(collection.find({}, {"_id":1, "primaryTitle":1, "review":1}))):
        graphDB.addMovie(movie['_id'], movie['primaryTitle'])
        movie['review'] = list({v['critic_name']:v for v in movie['review']}.values())
        for rev in movie['review']:
            graphDB.addReview(rev['critic_name'], movie['_id'], {"Fresh":"TRUE", "Rotten":"FALSE"}[rev['review_type']], str(rev['review_content'])[:15], str(rev['review_date'])[:10])
        print(f"{(i+1)/total:%}\r", end='')

    collection = dbname['user']
    uids = [x['_id'] for x in list(collection.find({"date_of_birth":{"$exists":"True"}}, {"_id":1}))]
    cids = [x['_id'] for x in list(collection.find({"date_of_birth":{"$exists":"False"}}, {"_id":1}))]
    total = len(cids)
    print(f"\nfollow {total = }")
    for i, critic in enumerate(cids):
        for _ in range(randint(0, 20)):
            choice(uids)
            graphDB.addFollow(choice(uids), critic)
        print(f"{i/total:%}")
    graphDB.close()

