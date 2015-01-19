package hr.fer.tel.moovis.searchers;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by filippm on 10.11.14..
 */
public abstract class GenericSearch implements Runnable {
	public static final Object SYNC_CONTROLLER = new Object();

	private static final String DB_NAME = "moovis";

	private DB db;
	private DBCollection movies;

	public GenericSearch() throws UnknownHostException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB(DB_NAME);

		movies = db.getCollection("movies");
	}

	public void run() {
		System.out.println(Thread.currentThread().getId());
		while (true) {
			searchProcess();
			try {
				Thread.sleep(getSleepTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void searchProcess() {
		DBCollection queue = getQueue(db);
		// bzbz
		DBCursor cursor = queue.find();
		DBObject obj;

		try {
			while (cursor.hasNext()) {
				// Dohvati iz reda
				obj = cursor.next();
				queue.remove(obj);
				System.out.println(obj);
				String movieKey = obj.get("movieKey").toString();
				int tmdbId = (int) obj.get("tmdbId");

				// Obrada uz TMDB api
				synchronized (SYNC_CONTROLLER) {
					// spremanje u bazu
					DBObject searchOldMovie = new BasicDBObject().append(
							"movieKey", movieKey).append("tmdbId", tmdbId);
					System.out.println("\t\t" + searchOldMovie);
					Cursor oldMovieCursor = movies.find(searchOldMovie);
					if (oldMovieCursor.hasNext()) {
						BasicDBObject oldMovieObject = (BasicDBObject) oldMovieCursor
								.next();
						BasicDBObject newMovieObject = (BasicDBObject) oldMovieObject
								.copy();
						processMovie(obj, newMovieObject);
						System.out.println("stari objekt\t"
								+ oldMovieObject.toMap());
						System.out.println("Novi objekt\t"
								+ newMovieObject.toMap());
						System.out.println(movies.update(oldMovieObject,
								newMovieObject).getN());

						postprocessActions(obj);
					}
					oldMovieCursor.close();
				}

			}
		} finally {
			cursor.close();
		}

	}

	protected void postprocessActions(DBObject obj) {
	}

	protected DB getDb() {
		return db;
	}

	protected abstract void processMovie(DBObject obj,
			BasicDBObject newMovieObject);

	protected abstract DBCollection getQueue(DB db);

	protected abstract long getSleepTime();
}
