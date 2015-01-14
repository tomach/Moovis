package hr.fer.tel.moovis.dao;

import java.net.UnknownHostException;

import hr.fer.tel.moovis.model.Movie;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Repository
public class MovieDao {

	private DBCollection movies;

	public MovieDao() throws UnknownHostException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");
		movies = db.getCollection("movies");
	}

	public Movie findMovieByName(String name) {
		Movie retVal=null;
		DBObject movieRecord = movies.findOne(new BasicDBObject("movieKey",
				name));
		if (movieRecord == null) {
			retVal = null;
		} else {
			
		}

		return retVal;
	}
}
