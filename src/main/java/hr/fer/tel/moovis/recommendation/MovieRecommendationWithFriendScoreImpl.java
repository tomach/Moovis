package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendationWithFriendScoreImpl implements
		MovieRecommendation {

	private static final double START_VALUE = 1.0;
	private static final double STEP = 1.0;

	@Autowired
	private MovieDao movieDao;

	@Override
	public List<RecommendationRecord> calculateRecommendation(
			ApplicationUser user) {

		Map<Movie, Set<RecommendationRecord>> similarMoviesOfLikedMovie = new HashMap<>();

		// get similar movies
		for (String likedMovieName : user.getLikedMovieNames()) {
			Movie likedMovie = movieDao.findMovieByName(likedMovieName);
			if (likedMovie == null) {
				System.out.println("Nema filma za naslov:" + likedMovieName);

				continue;
			}
			Set<RecommendationRecord> similarAsRecords = new HashSet<>();

			for (Movie sim : likedMovie.getSimilarMovies()) {
				similarAsRecords
						.add(new RecommendationRecord(sim, START_VALUE));
			}

			similarMoviesOfLikedMovie.put(likedMovie, similarAsRecords);
		}

		// all movies in one set
		Set<String> likedMovieNames = user.getLikedMovieNames();
		Set<RecommendationRecord> allSimilars = new HashSet<>();

		for (Set<RecommendationRecord> value : similarMoviesOfLikedMovie
				.values()) {
			for (RecommendationRecord val : value) {
				if (likedMovieNames.contains(val.getMovie().getTitle())) {
					continue;
				} else {
					allSimilars.add(val);
				}
			}
		}

		// get all friends and their liked movies
		List<String> allFriendMovies = new ArrayList<String>();
		Set<ApplicationUser> friends = user.getFriends();
		for (ApplicationUser friend : friends) {
			Set<String> friendsLikedMovies = friend.getLikedMovieNames();
			allFriendMovies.addAll(friendsLikedMovies);
		}

		// add STEP
		for (RecommendationRecord similar : allSimilars) {
			for (Set<RecommendationRecord> value : similarMoviesOfLikedMovie
					.values()) {
				if (value.contains(similar)) {
					similar.setRecScore(similar.getRecScore() + STEP);
				}
			}

			for (String friendsMovieName : allFriendMovies) {
				if (similar.getMovie().getTitle().equals(friendsMovieName)) {
					similar.setRecScore(similar.getRecScore() + STEP);
				}
			}
		}
		
		//add friends list  ---- TODO refactor
//		Set<RecommendationRecord> recordWithFriends = new HashSet<>();
//		Set<RecommendationRecord> deleteRecommendations = new HashSet<>();
		Set<RecommendationRecord> setWithFriends = new HashSet<>();
		
		for (RecommendationRecord recommendation : allSimilars) {
			boolean createdNewRecord = false;
			if (recommendation != null && recommendation.getMovie() != null
					&& recommendation.getMovie().getTitle() != null) {
				String movieName = recommendation.getMovie().getTitle();
				RecommendationRecordWithFriendLikes newRecord = null;
				
				for (ApplicationUser friend : friends) {
					if (friend.getLikedMovieNames().contains(movieName)) {
						//casted.addFriendName(builder.toString());
						if (newRecord == null) {
							newRecord = new RecommendationRecordWithFriendLikes(recommendation.getMovie(), recommendation.getRecScore());
							setWithFriends.add(newRecord);
							createdNewRecord = true;
							
						}
						StringBuilder builder = new StringBuilder();
						builder.append(friend.getName()).append(" ")
								.append(friend.getSurname());
						newRecord.addFriendName(builder.toString());
					}
				}
			}
			
			if (!createdNewRecord) {
				setWithFriends.add(recommendation);
			}
		}
		

		List<RecommendationRecord> finalRec = new LinkedList<>(setWithFriends);
		Collections.sort(finalRec);

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
		for (RecommendationRecord recommendationRecord : finalRec) {
			System.out.println(recommendationRecord.getMovie().getTitle()
					+ "\t" + recommendationRecord.getRecScore());
		}

		return finalRec;
	}
}