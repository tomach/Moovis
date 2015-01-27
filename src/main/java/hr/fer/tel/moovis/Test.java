package hr.fer.tel.moovis;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
		DBCollection tmdbSearchQueue = db.getCollection("TMDBSearchQueue");
		DBCollection youtubeSearchQueue = db.getCollection("YTSearchQueue");

		int i = 0;
		int j = 0;
		Cursor cur = movies.find();
		while (cur.hasNext()) {
			DBObject curObj = cur.next();

			if ((!curObj.containsField("tmdb") || !curObj
					.containsField("youTube"))
					&& curObj.containsField("tmdbId")
					&& curObj.containsField("movieKey")) {

				BasicDBObject insert = new BasicDBObject();
				insert.append("movieKey", curObj.get("movieKey"));
				insert.append("tmdbId", curObj.get("tmdbId"));
				tmdbSearchQueue.insert(insert);
				youtubeSearchQueue.insert(insert);

				i++;
			}
			// if (tmdb == null) {
			// if (curObj.containsField("movieKey")
			// && curObj.containsField("tmdbId")) {
			// /*
			// * BasicDBObject queueObj = new BasicDBObject();
			// * queueObj.append("movieKey", curObj.get("movieKey"));
			// * queueObj.append("tmdbId", curObj.get("tmdbId"));
			// * tmdbSearchQueue.insert(queueObj);
			// * youtubeSearchQueue.insert(queueObj);
			// */
			// }
			// j++;
			// } else {
			// if (tmdb.containsField("originalTitle")) {
			// if (!curObj
			// .get("movieKey")
			// .toString()
			// .toLowerCase()
			// .equals(tmdb.get("originalTitle").toString()
			// .toLowerCase())) {
			// i++;
			//
			// }
			// } else {
			// if (!curObj.get("movieKey").toString().toLowerCase()
			// .equals(tmdb.get("title").toString().toLowerCase())) {
			//
			// System.out.println();
			// System.out.println(curObj.get("movieKey"));
			// System.out.println(tmdb.get("title"));
			// if (curObj.containsField("imdb")) {
			// System.out.println(((DBObject) curObj.get("imdb"))
			// .get("title"));
			// }
			// i++;
			// }
			// }
			// }

		}
		System.out.println("Nisu isti:" + i);
		System.out.println("Nema tmdb:" + j);
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println("Loading over!");

	}
}
