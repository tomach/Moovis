package hr.fer.tel.moovis.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.tel.moovis.model.movie.IMDBMovieInfo;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.model.movie.MovieProxy;
import hr.fer.tel.moovis.model.movie.TMDBMovieInfo;
import hr.fer.tel.moovis.model.movie.YouTubeInfo;

import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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
		Movie retVal = null;
		DBObject movieRecord = movies.findOne(new BasicDBObject("movieKey",
				name));
		if (movieRecord == null) {
			retVal = null;
		} else {
			retVal = loadMovie(movieRecord);
		}

		return retVal;
	}

	public List<String> getSimilarMoviesNames(String name) {
		List<String> retList = null;
		DBObject movieRecord = movies.findOne(new BasicDBObject("movieKey",
				name));
		if (movieRecord == null) {
			retList = null;
		} else {
			if (movieRecord.containsField("tmdb")) {
				DBObject tmdb = (DBObject) movieRecord.get("tmdb");
				if (tmdb.containsField("similarMovies")) {
					BasicDBList similarMovies = (BasicDBList) tmdb
							.get("similarMovies");
					if (similarMovies != null) {
						retList = new ArrayList<>();
						for (Object object : similarMovies) {
							DBObject simMo = (DBObject) object;
							retList.add(simMo.get("title").toString());
						}
					}
				}
			}
		}
		return retList;
	}

	private Movie loadMovie(DBObject movieRecord) {

		String titme = movieRecord.get("movieKey").toString();

		TMDBMovieInfo tmbInfo = loadTMDBMovieInfo(movieRecord);
		IMDBMovieInfo imdbInfo = loadIMDBInfo(movieRecord);
		YouTubeInfo ytInfo = loadYTInfo(movieRecord);

		return new MovieProxy(titme, ytInfo, imdbInfo, tmbInfo, null, null,
				null, null);
	}

	private YouTubeInfo loadYTInfo(DBObject movieRecord) {
		YouTubeInfo ytInfo = new YouTubeInfo();
		if (movieRecord.containsField("youTube")) {
			movieRecord = (DBObject) movieRecord.get("youTube");
		} else {
			return null;
		}

		if (movieRecord.containsField("id")) {
			ytInfo.setId(movieRecord.get("id").toString());
		}

		if (movieRecord.containsField("description")) {
			ytInfo.setDescription(movieRecord.get("description").toString());
		}

		if (movieRecord.containsField("thumbnailURL")) {
			ytInfo.setThumbnailURL(movieRecord.get("thumbnailURL").toString());
		}

		if (movieRecord.containsField("title")) {
			ytInfo.setTitle(movieRecord.get("title").toString());
		}

		return ytInfo;
	}

	private IMDBMovieInfo loadIMDBInfo(DBObject movieRecord) {
		if (movieRecord.containsField("imdb")) {
			movieRecord = (DBObject) movieRecord.get("imdb");
		} else {
			return null;
		}
		IMDBMovieInfo imdbInfo = new IMDBMovieInfo();
		if (movieRecord.containsField("imdbId")) {
			imdbInfo.setImdbId(movieRecord.get("imdbId").toString());
		}

		if (movieRecord.containsField("rating")) {
			imdbInfo.setRating(Double.parseDouble(movieRecord.get("rating")
					.toString()));
		}
		return imdbInfo;
	}

	private TMDBMovieInfo loadTMDBMovieInfo(DBObject movieRecord) {
		if (movieRecord.containsField("tmdb")) {
			movieRecord = (DBObject) movieRecord.get("tmdb");
		} else {
			return null;
		}
		TMDBMovieInfo tmdbInfo = new TMDBMovieInfo();
		if (movieRecord.containsField("id")) {
			tmdbInfo.setTmdbId(movieRecord.get("id").toString());
		}
		if (movieRecord.containsField("budget")) {
			tmdbInfo.setBudget(Long.parseLong(movieRecord.get("budget")
					.toString()));
		}
		if (movieRecord.containsField("revenue")) {
			tmdbInfo.setRevenue(Long.parseLong(movieRecord.get("revenue")
					.toString()));
		}
		if (movieRecord.containsField("releaseDate")) {
			tmdbInfo.setReleaseDate(movieRecord.get("releaseDate").toString());
		}
		if (movieRecord.containsField("overview")) {
			tmdbInfo.setOverview(movieRecord.get("overview").toString());
		}

		if (movieRecord.containsField("voteAverage")) {
			tmdbInfo.setVoteAverage(Double.parseDouble(movieRecord.get(
					"voteAverage").toString()));
		}
		if (movieRecord.containsField("voteCount")) {
			tmdbInfo.setVoteCount(Long.parseLong(movieRecord.get("voteCount")
					.toString()));
		}
		return tmdbInfo;
	}
}
