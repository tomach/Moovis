package hr.fer.tel.moovis;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Test {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");
		DBCollection movies = db.getCollection("movies");

		DBObject movie = movies.findOne(new BasicDBObject("movieKey",
				"Interstellar"));
		System.out.println(movie.containsField("tmdb.id"));
	}

}
