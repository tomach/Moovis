package hr.fer.tel.moovis.searchers;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbCast;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;
import com.omertron.themoviedbapi.MovieDbException;

public class DirectorInfoCollector {
	private static final String DB_NAME = "moovis";

	private DB db;
	private DBCollection directorInfoCollelction;
	private DBCollection movies;

	public DirectorInfoCollector() throws UnknownHostException,
			MovieDbException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB(DB_NAME);

		directorInfoCollelction = db.getCollection("DirectorInfo");
		movies = db.getCollection("movies");

	}

	public void process() {
		int i = 0;
		DBCursor cur = movies.find();
		cur.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
		long startTime;
		long endTime;
		while (cur.hasNext()) {
			startTime = System.currentTimeMillis();
			i++;
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			DBObject movie = cur.next();
			if (!movie.containsField("imdb")) {
				continue;
			}
			DBObject imdb = (DBObject) movie.get("imdb");

			if (!imdb.containsField("directors")) {
				continue;
			}

			// provjeri da vec nije updatean
			if (imdb.containsField("directors")) {
				BasicDBList tempDirs = (BasicDBList) imdb.get("directors");
				if (tempDirs.size() > 0) {
					DBObject dir = (DBObject) tempDirs.get(0);
					if (dir.keySet().size() != 1) {
						continue;
					}
				}
			}

			final String imdbId = imdb.get("imdbId").toString();

			// IMDB dohvat
			ImdbMovieDetails movieImdbInfo = null;

			// HACK
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Callable<Object> task = new Callable<Object>() {
				public Object call() {
					return ImdbApi.getFullDetails(imdbId);
				}
			};
			Future<Object> future = executor.submit(task);
			try {
				movieImdbInfo = (ImdbMovieDetails) future.get(5,
						TimeUnit.SECONDS);
			} catch (TimeoutException ex) {
				// handle the timeout
				System.err.println("Timeout");
				continue;
			} catch (InterruptedException e) {
				// handle the interrupts
				return;
			} catch (ExecutionException e) {
				// handle other exceptions
				return;
			} finally {
				future.cancel(true); // may or may not desire this
			}
			// END HACK

			if (movieImdbInfo == null) {
				System.err.println("NULL result :(");
				return;
			}

			// Update existing movies
			BasicDBList directors = new BasicDBList();
			for (ImdbCast director : movieImdbInfo.getDirectors()) {
				directors.add(new BasicDBObject("name", director.getPerson()
						.getName()).append("imdbId", director.getPerson()
						.getActorId()));
			}

			movies.update(movie, new BasicDBObject("$set", new BasicDBObject(
					"imdb.directors", directors)));

			// add director info to directorInfoCollection
			for (ImdbCast director : movieImdbInfo.getDirectors()) {
				BasicDBObject directorInfo = new BasicDBObject();

				directorInfo
						.append("imdbId", director.getPerson().getActorId());
				// ako vec postoji taj redatelj
				if (directorInfoCollelction.findOne(directorInfo) != null) {
					continue;
				}
				directorInfo.append("name", director.getPerson().getName());
				directorInfo.append("biography", director.getPerson()
						.getBiography());

				if (director.getPerson().getImage() != null) {
					directorInfo.append("photo", director.getPerson()
							.getImage().getUrl());
				}

				directorInfoCollelction.insert(directorInfo);
			}
			endTime = System.currentTimeMillis();

			//System.out.println("Execution time:" + (endTime - startTime));
		}
	}

	public static void main(String[] args) throws UnknownHostException,
			MovieDbException {
		new DirectorInfoCollector().process();
	}
}
