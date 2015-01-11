package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbCast;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;

import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by tomislaf on 10.11.2014..
 */
public class IMDBSearcher extends GenericSearch {
	public static final String IMDB_SEARCH_QUEUE = "IMDBSearchQueue";
	protected volatile String currId = "fsa";

	public IMDBSearcher() throws UnknownHostException {
		super();
	}

	protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
		String movieKey = obj.get("movieKey").toString();
		final String imdbId = obj.get("imdbId").toString();

		System.out.println(movieKey);
		System.out.println(imdbId);
		currId = imdbId;
		// IMDB dohvat
		ImdbMovieDetails movie = null;

		// HACK
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Object> task = new Callable<Object>() {
			public Object call() {
				return ImdbApi.getFullDetails(imdbId);
			}
		};
		Future<Object> future = executor.submit(task);
		try {
			movie = (ImdbMovieDetails) future.get(5, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			// handle the timeout
			System.err.println("Timeout");
			return;
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
		System.out.println("IMDB search:" + movie);

		if (movie == null) {
			System.err.println("NULL result :(");
			return;
		}
		DBObject plot = new BasicDBObject()
				.append("outline", movie.getBestPlot().getOutline())
				.append("summary", movie.getBestPlot().getSummary())
				.append("author", movie.getBestPlot().getAuthor())
				.append("text", movie.getBestPlot().getText());
		BasicDBList cast = new BasicDBList();
		for (ImdbCast ca : movie.getCast()) {
			cast.add(new BasicDBObject().append("id",
					ca.getPerson().getActorId()).append("name",
					ca.getPerson().getName()));
		}

		BasicDBList genres = new BasicDBList();
		for (String g : movie.getGenres()) {
			genres.add(g);
		}
		BasicDBList directors = new BasicDBList();
		for (ImdbCast director : movie.getDirectors()) {
			directors.add(new BasicDBObject("name", director.getPerson()
					.getName()));
		}

		DBObject movieDetails = new BasicDBObject()
				.append("imdbId", movie.getImdbId())
				.append("title", movie.getTitle())
				.append("rating", movie.getRating())
				.append("year", movie.getYear()).append("cast", cast)
				.append("plot", plot).append("genres", genres)
				.append("directors", directors);
		newMovieObject.append("imdb", movieDetails);

	}

	@Override
	protected DBCollection getQueue(DB db) {
		return db.getCollection(IMDB_SEARCH_QUEUE);
	}

	@Override
	protected long getSleepTime() {
		return 1000;
	}

	public static void main(String[] argv) throws UnknownHostException {

		IMDBSearcher ser = new IMDBSearcher();
		Thread newSearc = new Thread(ser);
		newSearc.start();

		

		/*
		 * while (true) { IMDBSearcher ser = new IMDBSearcher(); Thread newSearc
		 * = new Thread(ser); newSearc.start(); String currId = ""; while (true)
		 * { try { Thread.sleep(5000); } catch (InterruptedException e) { } if
		 * (currId.equals(ser.currId)) { break; } else { currId = ser.currId; }
		 * }
		 * 
		 * newSearc.interrupt(); System.err.println("Thread interupred"); }
		 */

	}
}
