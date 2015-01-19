package hr.fer.tel.moovis.searchers;

import java.net.UnknownHostException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ActorQueueFill {

	private static final String DB_NAME = "moovis";

	private DB db;
	private DBCollection movies;
	private DBCollection actorInfoQueue;
	private DBCollection actorInfo;

	public ActorQueueFill() throws UnknownHostException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB(DB_NAME);

		movies = db.getCollection("movies");
		actorInfoQueue = db.getCollection("ActorInfoQueue");
		actorInfo = db.getCollection("ActorInfo");

	}

	public void process() {
		Cursor cur = movies.find();
		int i = 0;
		int isti = 0;
		int novi = 0;
		while (cur.hasNext()) {
			i++;
			if (i % 1000 == 0) {
				System.out.println(i);
				System.out.println("novi:" + novi);
			}
			DBObject movie = cur.next();

			if (!movie.containsField("tmdb")) {
				continue;
			}
			DBObject tmdb = (DBObject) movie.get("tmdb");

			if (!tmdb.containsField("cast")) {
				continue;
			}

			BasicDBList cast = (BasicDBList) tmdb.get("cast");

			for (Object obj : cast) {
				DBObject actor = (DBObject) obj;
				DBObject checkActorQueue = new BasicDBObject("tmdbId",
						actor.get("id"));
				if (actorInfoQueue.findOne(checkActorQueue) == null
						&& actorInfo.findOne(checkActorQueue) == null) {
					actorInfoQueue.insert(checkActorQueue);
					novi++;
				} else {
					isti++;
					continue;
				}
			}
		}
		System.out.println("isti:" + isti);
	}

	public static void main(String[] args) throws UnknownHostException {
		new ActorQueueFill().process();
	}

}
