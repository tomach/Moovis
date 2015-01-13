package hr.fer.tel.moovis.main;

import java.net.UnknownHostException;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MoovisChecker {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");

		DBCollection movies = db.getCollection("movies");

		Cursor cur = movies.find();
		int notImdb = 0;
		int notTmdb = 0;
		int notYT = 0;
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			if (!obj.keySet().contains("tmdb")) {
				notTmdb++;
			}
			if (!obj.keySet().contains("imdb")) {
				notImdb++;
			}
			if (!obj.keySet().contains("youTube")) {
				notYT++;
			}
		}
		System.out.println("NotImdb:" + notImdb);
		System.out.println("NotTmdb:" + notTmdb);
		System.out.println("NotYT:" + notYT);

	}

}
