package hr.fer.tel.moovis;

import java.net.UnknownHostException;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;

public class Test {

	public static void main(String[] args) throws MovieDbException,
			UnknownHostException {

		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");

		DBCollection movies = db.getCollection("movies");
		int i = 0;
		Cursor cur = movies.find();
		while (cur.hasNext()) {
			DBObject curObj = cur.next();
			if (curObj.containsField("tmdb")) {
				DBObject tmdb = (DBObject) curObj.get("tmdb");
				if (!tmdb
						.get("title")
						.toString()
						.toLowerCase()
						.equals(curObj.get("movieKey").toString().toLowerCase())) {

					i++;
				}
			}
		}
		System.out.println(i++);
		System.out.println("Loading over!");

	}
}
