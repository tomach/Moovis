package hr.fer.tel.moovis.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hr.fer.tel.moovis.model.Person;
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
	private DBCollection actors;
	private DBCollection directors;

	public MovieDao() throws UnknownHostException {
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");
		movies = db.getCollection("movies");
		actors = db.getCollection("ActorInfo");
		directors = db.getCollection("DirectorInfo");
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
		List<String> genres = loadGenres(movieRecord);
		List<Person> actors = loadActors(movieRecord);
		List<Person> directors = loadDirectors(movieRecord);
		return new MovieProxy(titme, ytInfo, imdbInfo, tmbInfo, genres, actors,
				directors, null);
	}

	private List<Person> loadDirectors(DBObject movieRecord) {
		Set<Person> retList = new HashSet<>();
		if (movieRecord.containsField("imdb")) {
			DBObject imdb = (DBObject) movieRecord.get("imdb");
			if (imdb.containsField("directors")) {
				BasicDBList cast = (BasicDBList) imdb.get("directors");
				for (Object object : cast) {
					DBObject director = (DBObject) object;
					Person actorPerson = loadDirector(director.get("imdbId"));
					if (actorPerson != null) {
						retList.add(actorPerson);
					}
				}
			}
		}

		return new LinkedList<Person>(retList);
	}

	private Person loadDirector(Object object) {
		String id = object.toString();

		DBObject directorRec = directors
				.findOne(new BasicDBObject("imdbId", id));
		if (directorRec == null) {
			return null;
		}
		String name = null;
		String biography = null;
		String photo = null;
		if (directorRec.containsField("name")) {
			name = directorRec.get("name").toString();
		}
		if (directorRec.containsField("biography")) {
			biography = directorRec.get("biography").toString();
		}
		if (directorRec.containsField("photo")) {
			photo = directorRec.get("photo").toString();
		}

		return new Person("" + id, name, biography, photo, null);
	}

	private List<Person> loadActors(DBObject movieRecord) {
		Set<Person> retList = new HashSet<>();
		if (movieRecord.containsField("tmdb")) {
			DBObject tmdb = (DBObject) movieRecord.get("tmdb");
			if (tmdb.containsField("cast")) {
				BasicDBList cast = (BasicDBList) tmdb.get("cast");
				for (Object object : cast) {
					DBObject actor = (DBObject) object;
					Person actorPerson = loadActor(actor.get("id"));
					if (actorPerson != null) {
						retList.add(actorPerson);
					}
				}
			}
		}

		return new LinkedList<Person>(retList);
	}

	private Person loadActor(Object object) {
		Long id;
		try {
			id = Long.parseLong(object.toString());
		} catch (Exception e) {
			return null;
		}
		DBObject actorRec = actors.findOne(new BasicDBObject("tmdbId", id));
		if (actorRec == null) {
			return null;
		}
		String name = null;
		String biography = null;
		String photo = null;
		String birthdate = null;
		if (actorRec.containsField("name")) {
			name = actorRec.get("name").toString();
		}
		if (actorRec.containsField("biography")) {
			biography = actorRec.get("biography").toString();
		}
		if (actorRec.containsField("picture")) {
			photo = actorRec.get("picture").toString();
		}
		if (actorRec.containsField("birthday")) {
			birthdate = actorRec.get("birthday").toString();
		}
		return new Person("" + id, name, biography, photo, birthdate);
	}

	private List<String> loadGenres(DBObject movieRecord) {
		Set<String> genres = new HashSet<>();
		if (movieRecord.containsField("tmdb")) {
			DBObject tmdb = (DBObject) movieRecord.get("tmdb");
			if (tmdb.containsField("genres")) {
				BasicDBList genList = (BasicDBList) tmdb.get("genres");
				for (Object object : genList) {
					genres.add(object.toString());
				}
			}
		}

		if (movieRecord.containsField("imdb")) {
			DBObject imdb = (DBObject) movieRecord.get("imdb");
			if (imdb.containsField("genres")) {
				BasicDBList genList = (BasicDBList) imdb.get("genres");
				for (Object object : genList) {
					genres.add(object.toString());
				}
			}
		}

		return new LinkedList<String>(genres);
	}

	private YouTubeInfo loadYTInfo(DBObject movieRecord) {
		YouTubeInfo ytInfo = new YouTubeInfo();
		if (movieRecord.containsField("youTube")
				&& movieRecord.get("youTube") != null) {
			movieRecord = (DBObject) movieRecord.get("youTube");
		} else {
			return null;
		}

		if (movieRecord.containsField("id") && movieRecord.get("id") != null) {
			ytInfo.setId(movieRecord.get("id").toString());
		}

		if (movieRecord.containsField("description")
				&& movieRecord.get("description") != null) {
			ytInfo.setDescription(movieRecord.get("description").toString());
		}

		if (movieRecord.containsField("thumbnailURL")
				&& movieRecord.get("thumbnailURL") != null) {
			ytInfo.setThumbnailURL(movieRecord.get("thumbnailURL").toString());
		}

		if (movieRecord.containsField("title")
				&& movieRecord.get("title") != null) {
			ytInfo.setTitle(movieRecord.get("title").toString());
		}

		return ytInfo;
	}

	private IMDBMovieInfo loadIMDBInfo(DBObject movieRecord) {
		if (movieRecord.containsField("imdb")
				&& movieRecord.get("imdb") != null) {
			movieRecord = (DBObject) movieRecord.get("imdb");
		} else {
			return null;
		}
		IMDBMovieInfo imdbInfo = new IMDBMovieInfo();
		if (movieRecord.containsField("imdbId")
				&& movieRecord.get("imdbId") != null) {
			imdbInfo.setImdbId(movieRecord.get("imdbId").toString());
		}

		if (movieRecord.containsField("rating")
				&& movieRecord.get("rating") != null) {
			imdbInfo.setRating(Double.parseDouble(movieRecord.get("rating")
					.toString()));
		}
		return imdbInfo;
	}

	private TMDBMovieInfo loadTMDBMovieInfo(DBObject movieRecord) {
		if (movieRecord.containsField("tmdb")
				&& movieRecord.get("tmdb") != null) {
			movieRecord = (DBObject) movieRecord.get("tmdb");
		} else {
			return null;
		}

		TMDBMovieInfo tmdbInfo = new TMDBMovieInfo();
		if (movieRecord.containsField("id") && movieRecord.get("id") != null) {
			tmdbInfo.setTmdbId(movieRecord.get("id").toString());
		}
		if (movieRecord.containsField("budget")
				&& movieRecord.get("budget") != null) {
			tmdbInfo.setBudget(Long.parseLong(movieRecord.get("budget")
					.toString()));
		}
		if (movieRecord.containsField("revenue")
				&& movieRecord.get("revenue") != null) {
			tmdbInfo.setRevenue(Long.parseLong(movieRecord.get("revenue")
					.toString()));
		}
		if (movieRecord.containsField("releaseDate")
				&& movieRecord.get("releaseDate") != null) {
			tmdbInfo.setReleaseDate(movieRecord.get("releaseDate").toString());
		}
		if (movieRecord.containsField("overview")
				&& movieRecord.get("overview") != null) {
			tmdbInfo.setOverview(movieRecord.get("overview").toString());
		}

		if (movieRecord.containsField("voteAverage")
				&& movieRecord.get("voteAverage") != null) {
			tmdbInfo.setVoteAverage(Double.parseDouble(movieRecord.get(
					"voteAverage").toString()));
		}
		if (movieRecord.containsField("voteCount")
				&& movieRecord.get("voteCount") != null) {
			tmdbInfo.setVoteCount(Long.parseLong(movieRecord.get("voteCount")
					.toString()));
		}
		return tmdbInfo;
	}
}
