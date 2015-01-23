package hr.fer.tel.moovis.service;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import facebook4j.FacebookException;
import facebook4j.Movie;
import hr.fer.tel.moovis.MongoConnections;
import hr.fer.tel.moovis.apis.FacebookAPI;
import hr.fer.tel.moovis.dao.ApplicationUserDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.names.MovieNamesContainer;
import hr.fer.tel.moovis.web.controllers.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

@Service
@Transactional
public class FacebookProfileUpdater {
	@Autowired
	private ApplicationUserDao appUserDao;

	private DBCollection rottenQueue;

	public FacebookProfileUpdater() {
		DB db = MongoConnections.getInstance().getDb();
		rottenQueue = db.getCollection("RottenSearchQueue");
	}

	public ApplicationUser updateUserLikes(
			ApplicationUser applicationUserLazyLoaded) {
		try {
			FacebookAPI faceApi = new FacebookAPI(
					applicationUserLazyLoaded.getFacebookAccessToken());
			Set<String> likedMovieNames = getAllMovieNames(faceApi
					.getMovies(applicationUserLazyLoaded
							.getLastMovieLikesUpdateTS()));
			// avoid lazy loading exception
			applicationUserLazyLoaded = appUserDao
					.findOne(applicationUserLazyLoaded.getId());

			if (likedMovieNames != null && likedMovieNames.size() > 0) {

				System.out.println(Logger.getLogString(
						System.currentTimeMillis(),
						"FacebookLikesUpdater process"));
				MovieNamesContainer movieNamesChecker = MovieNamesContainer
						.getInstance();
				System.out.println(Logger.getLogString(
						System.currentTimeMillis(), "Usporedba slicnosti!"));
				for (String movie : likedMovieNames) {
					String checkedName = movieNamesChecker.getMovieName(movie);
					System.out.println(movie);
					System.out.println(checkedName);
					applicationUserLazyLoaded.addLikedMovie(checkedName);
					rottenQueue.insert(new BasicDBObject("movieKey",
							checkedName));
				}
				applicationUserLazyLoaded.setLastMovieLikesUpdateTS(System
						.currentTimeMillis() - 1000 * 60);
			}
		} catch (Exception e) {
			System.out.println(Logger.getLogString(System.currentTimeMillis(),
					"FacebookLikesUpdater error:" + e.getLocalizedMessage()));
		}
		return applicationUserLazyLoaded;
	}

	private Set<String> getAllMovieNames(List<Movie> movies) {
		Set<String> movieNames = new HashSet<>();
		for (Movie movie : movies) {
			movieNames.add(movie.getName());
		}
		return movieNames;
	}
}
