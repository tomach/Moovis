package hr.fer.tel.moovis.searchers.rotten;

import hr.fer.tel.moovis.MongoConnections;
import hr.fer.tel.moovis.names.MovieNamesContainer;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.omertron.rottentomatoesapi.RottenTomatoesApi;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.RTMovie;

public class RottenCollector implements Runnable {

	private static final String API_KEY = "sdsque4c4t6gynrtv8znmrrz";
	private static final String ROTTEN_COLLECTION = "RottenSearchQueue";

	private RottenApiProxy rottenApi;

	private DB db;
	private DBCollection movies;
	private DBCollection rottenQueueCol;

	public RottenCollector() throws UnknownHostException,
			RottenTomatoesException {
		db = MongoConnections.getInstance().getDb();

		movies = db.getCollection("movies");
		rottenQueueCol = db.getCollection(ROTTEN_COLLECTION);

		RottenApiProxy.setApiKey(API_KEY);
		rottenApi = RottenApiProxy.getInstance();
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId());
		while (true) {
			process();
			System.out.println("RottenQueue emtpy. Sleep 10 s");
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void process() {
		DBCursor rottenQueueCursor = rottenQueueCol.find();
		rottenQueueCursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

		while (rottenQueueCursor.hasNext()) {
			DBObject queueObject = rottenQueueCursor.next();
			String movieKey = queueObject.get("movieKey").toString();

			BasicDBObject movieColFinder = new BasicDBObject("movieKey",
					movieKey);
			DBObject movieRec = movies.findOne(movieColFinder);
			// ako je zapis vec obraden od rottena makni ga iz queuea i continue
			if (movieRec != null && movieRec.containsField("rottenInfo")) {
				rottenQueueCol.remove(queueObject);
				continue;
			}
			// obrada s rottenom
			List<RTMovie> moviesSearch = null;
			try {
				moviesSearch = rottenApi.getMoviesSearch(movieKey);
			} catch (RottenTomatoesException e) {
				e.printStackTrace();
			}
			if (moviesSearch != null & moviesSearch.size() > 0) {
				RTMovie rtMovie = moviesSearch.get(0);
				System.out.println("Rotten search result:" + rtMovie);

				List<RTMovie> similarMovies = null;
				try {
					similarMovies = rottenApi.getMoviesSimilar(rtMovie.getId(),
							5);
				} catch (RottenTomatoesException e) {
					e.printStackTrace();
				}
				BasicDBObject rottenInfo = new BasicDBObject();
				rottenInfo.put("id", rtMovie.getId());
				rottenInfo.put("title", rtMovie.getTitle());
				if (rtMovie.getRatings().containsKey("critics_score")) {
					rottenInfo.put("critics_score",
							rtMovie.getRatings().get("critics_score"));
				}
				if (rtMovie.getRatings().containsKey("audience_score")) {
					rottenInfo.put("audience_score",
							rtMovie.getRatings().get("audience_score"));
				}
				if (similarMovies != null) {
					BasicDBList similarList = new BasicDBList();
					for (RTMovie similarMovie : similarMovies) {
						BasicDBObject similarRottenInfo = new BasicDBObject();
						similarRottenInfo.put("id", similarMovie.getId());

						String normalizedSimMovieName = MovieNamesContainer
								.getInstance().getMovieName(
										similarMovie.getTitle());

						similarRottenInfo.put("normalizedTitle",
								normalizedSimMovieName);
						similarRottenInfo.put("title", similarMovie.getTitle());

						if (similarMovie.getRatings().containsKey(
								"critics_score")) {
							similarRottenInfo.put("critics_score", similarMovie
									.getRatings().get("critics_score"));
						}
						if (similarMovie.getRatings().containsKey(
								"audience_score")) {
							similarRottenInfo.put(
									"audience_score",
									similarMovie.getRatings().get(
											"audience_score"));
						}
						similarList.add(similarRottenInfo);
					}
					rottenInfo.put("similar", similarList);
				}

				// Updejtaj zapis u movie kolekciji
				BasicDBObject rottenInfoRoot = new BasicDBObject("rottenInfo",
						rottenInfo);
				movies.update(movieColFinder, new BasicDBObject("$set",
						rottenInfoRoot));
				rottenQueueCol.remove(queueObject);
			} else {
				if (queueObject.containsField("tries")) {
					int triesNo = (int) queueObject.get("tries");
					triesNo++;
					if (triesNo >= 3) {
						rottenQueueCol.remove(queueObject);
					} else {
						rottenQueueCol.update(queueObject, new BasicDBObject(
								"$set", new BasicDBObject("tries", triesNo)));
					}
				} else {
					rottenQueueCol.update(queueObject, new BasicDBObject(
							"$set", new BasicDBObject("tries", 1)));
				}
			}
		}
	}

	public static void main(String[] args) throws RottenTomatoesException,
			UnknownHostException {
		System.out.println("Starting rotten..");
		new Thread(new RottenCollector()).start();
		System.out.println("Rotten started!");
	}

}
