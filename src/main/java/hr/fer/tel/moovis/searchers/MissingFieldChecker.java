package hr.fer.tel.moovis.searchers;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MissingFieldChecker {
	private DB db;

	public MissingFieldChecker() throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB("moovis");
	}

	public void checkMovies() {

		DBCollection movies = db.getCollection("movies");

		Cursor cur = movies.find();
		DBCollection tmdbSearchQueue = db.getCollection("TMDBSearchQueue");
		DBCollection ytbSearchQueue = db.getCollection("YTSearchQueue");
		while (cur.hasNext()) {
			DBObject obj = cur.next();

			DBObject queueObj = new BasicDBObject().append("movieKey",
					obj.get("movieKey")).append("name", obj.get("name"));

			if (!obj.keySet().contains("tmdb")) {
				System.out.println(queueObj);
				tmdbSearchQueue.insert(queueObj);
			}
			if (!obj.keySet().contains("youTube")) {
				System.out.println(queueObj);
				ytbSearchQueue.insert(queueObj);
			}
		}

	}

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		new MissingFieldChecker().checkMovies();
	}

}
