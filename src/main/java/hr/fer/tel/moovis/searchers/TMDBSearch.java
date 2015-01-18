package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.MovieDb;
import com.omertron.themoviedbapi.model.Person;
import com.omertron.themoviedbapi.results.TmdbResultsList;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippm on 10.11.14..
 */
public class TMDBSearch extends GenericSearch {

	private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";
	private static final String MY_QUEUE = "TMDBSearchQueue";

	private TheMovieDbApi theMovieDbApi;

	public TMDBSearch() throws MovieDbException, UnknownHostException {
		super();
		theMovieDbApi = new TheMovieDbApi(API_KEY);
	}

	@Override
	protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
		String movieKey = obj.get("movieKey").toString();
		int movieId = (int) obj.get("tmdbId");
		System.out.println(movieKey);
		System.out.println(movieId);
		// Obrada uz TMDB api
		try {

			MovieDb movie = theMovieDbApi.getMovieInfo(movieId, null, "");

			System.out.println("THDB search result:" + movie);
			BasicDBObject movieDetails = new BasicDBObject()
					.append("id", movie.getId())
					.append("title", movie.getTitle())
					.append("budget", movie.getBudget())
					.append("popularity", movie.getPopularity())
					.append("userRating", movie.getUserRating())
					.append("voteAverage", movie.getVoteAverage())
					.append("homepage", movie.getHomepage())
					.append("revenue", movie.getRevenue())
					.append("releaseDate", movie.getReleaseDate())
					.append("voteCount", movie.getVoteCount())
					.append("imdbID", movie.getImdbID())
					.append("overview", movie.getOverview())
					.append("photo", movie.getPosterPath())
					.append("backdropPath", movie.getBackdropPath())
					.append("updatedPhotos", true);
			if (movie.getGenres() != null) {
				List<String> genres = new ArrayList<String>();
				for (Genre genre : movie.getGenres()) {
					genres.add(genre.getName());
				}
				movieDetails.append("genres", genres);
			}

			TmdbResultsList<Person> movieCasts = theMovieDbApi
					.getMovieCasts(movie.getId());
			if (movieCasts != null) {
				List<BasicDBObject> cast = new ArrayList<BasicDBObject>();
				int castCounter = 0;
				for (Person person : movieCasts.getResults()) {
					if (castCounter > 10) {
						break;
					}
					castCounter++;
					cast.add(new BasicDBObject().append("id", person.getId())
							.append("name", person.getName())
							.append("character", person.getCharacter()));
				}
				movieDetails.append("cast", cast);
			}

			try {
				BasicDBList similarMoviesList = new BasicDBList();
				TmdbResultsList<MovieDb> simMovies = theMovieDbApi
						.getSimilarMovies(movie.getId(), null, 0, "");
				System.out.println("Similar movies:" + simMovies);
				// DBCollection similarMoviesCollection =
				// getDb().getCollection("SimilarMovies");
				for (MovieDb simMovie : simMovies.getResults()) {
					BasicDBObject simMovieDetails = new BasicDBObject().append(
							"id", simMovie.getId()).append("title",
							simMovie.getTitle());
					similarMoviesList.add(simMovieDetails);
					// similarMoviesCollection.insert(new
					// BasicDBObject("id",simMovie.getId()));
				}
				movieDetails.append("similarMovies", similarMoviesList);

			} catch (Exception e) {
			}

			newMovieObject.append("tmdb", movieDetails);

			String imdbId = movie.getImdbID();
			if (imdbId != null && !imdbId.isEmpty()) {
				System.out.println("Get imdbId and put it to IMDB queue");
				DB db = getDb();

				BasicDBObject imdbObject = new BasicDBObject().append(
						"movieKey", movieKey).append("imdbId", imdbId);
				DBCollection table = db.getCollection("IMDBSearchQueue");
				table.insert(imdbObject);
			}

		} catch (MovieDbException e) {
		}
	}

	@Override
	protected DBCollection getQueue(DB db) {
		return db.getCollection(MY_QUEUE);
	}

	@Override
	protected long getSleepTime() {
		return 330;
	}

	public static void main(String[] args) throws MovieDbException,
			UnknownHostException {
		new Thread(new TMDBSearch()).start();

	}
}
