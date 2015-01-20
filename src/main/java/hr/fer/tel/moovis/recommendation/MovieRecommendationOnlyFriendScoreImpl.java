package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.service.RecomendationResponseNesto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendationOnlyFriendScoreImpl implements
		MovieRecommendation {

	private static final double START_VALUE = 1.0;
	private static final double STEP = 1.0;

	@Autowired
	private RecomendationResponseNesto recommendaionNesto;

	@Autowired
	private MovieDao movieDao;

	// @Autowired
	// private ApplicationUserRepository userRepo;

	@Override
	public List<RecommendationRecord> calculateRecommendation(
			ApplicationUser user) {

		if (user == null) {
			return null;
		}

		// get user movies
		Set<String> userMovies = user.getLikedMovieNames();

		// get all friends and their liked movies
		List<String> allFriendMovies = new ArrayList<String>();
		Set<ApplicationUser> friends = user.getFriends();
		for (ApplicationUser friend : friends) {
			Set<String> friendsLikedMovies = friend.getLikedMovieNames();
			allFriendMovies.addAll(friendsLikedMovies);
		}

		// allFriendMovies = new ArrayList<String>();
		// allFriendMovies.add("Film1");
		// allFriendMovies.add("Film1");
		// allFriendMovies.add("Film2");
		// allFriendMovies.add("Film3");
		// allFriendMovies.add("Film4");
		// allFriendMovies.add("Film4");
		//
		// userMovies = new HashSet<String>();
		// userMovies.add("Film2");
		// userMovies.add("Film1");
		// userMovies.add("Film5");

		// exclude watched movies
		allFriendMovies.removeAll(userMovies);

		// List<String> union = new ArrayList<String>(allFriendMovies);
		// union.addAll(userMovies);
		//
		// List<String> intersection = new ArrayList<String>(allFriendMovies);
		// intersection.retainAll(userMovies);
		//
		// for (String name : userMovies) {
		// System.out.println("Moje: " + name);
		// }
		//
		// for (String name : allFriendMovies) {
		// System.out.println("Njihovo: " + name);
		// }
		//
		//
		// union.removeAll(intersection);

		// create initial records
		Set<String> movieNames = new HashSet<String>(allFriendMovies);
		Set<RecommendationRecord> friendsMovieRec = new HashSet<>();
		for (String friendsMovie : movieNames) {
			Movie likedMovie = movieDao.findMovieByName(friendsMovie);
			if (likedMovie != null) {
				friendsMovieRec.add(new RecommendationRecordWithFriendLikes(
						likedMovie, START_VALUE));
			} else {
				System.out.println("Nema filma za naslov:" + friendsMovie);
			}
		}

		// add STEP
		for (RecommendationRecord recommendation : friendsMovieRec) {
			for (String friendsMovieName : allFriendMovies) {
				if (recommendation != null && recommendation.getMovie() != null
						&& recommendation.getMovie().getTitle() != null) {
					if (recommendation.getMovie().getTitle()
							.equals(friendsMovieName)) {
						recommendation.setRecScore(recommendation.getRecScore()
								+ STEP);
					}
				}
			}
		}

		// add friends
		for (RecommendationRecord recommendation : friendsMovieRec) {
			if (recommendation != null && recommendation.getMovie() != null
					&& recommendation.getMovie().getTitle() != null) {
				String movieName = recommendation.getMovie().getTitle();
				RecommendationRecordWithFriendLikes casted = (RecommendationRecordWithFriendLikes) recommendation;
				for (ApplicationUser friend : friends) {
					if (friend.getLikedMovieNames().contains(movieName)) {
						StringBuilder builder = new StringBuilder();
						builder.append(friend.getName()).append(" ")
								.append(friend.getSurname());
						casted.addFriendName(builder.toString());
					}
				}
			}
		}

		Set<RecommendationRecord> filteredSet = recommendaionNesto
				.removeWatchedAndLikedMovies(friendsMovieRec, user);

		List<RecommendationRecord> finalRec = new LinkedList<>(filteredSet);
		Collections.sort(finalRec);

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
		for (RecommendationRecord recommendationRecord : finalRec) {
			if (recommendationRecord != null
					&& recommendationRecord.getMovie() != null
					&& recommendationRecord.getMovie().getTitle() != null) {
				RecommendationRecordWithFriendLikes recRecord = (RecommendationRecordWithFriendLikes) recommendationRecord;
				System.out.println(recRecord.getMovie().getTitle() + "\t"
						+ recRecord.getRecScore() + "\t"
						+ recRecord.getFriendNames());
			}
		}

		return finalRec;
	}
}