package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.service.FriendsLikesService;

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
public class MovieRecommendationImpl implements MovieRecommendation {
	@Autowired
	private MovieDao movieDao;
	@Autowired
	private FriendsLikesService friendsService;

	@Override
	public List<RecommendationRecord> calculateRecommendation(
			ApplicationUser user) {
		Map<Movie, Set<RecommendationRecord>> similarsForLikedMovies = new HashMap<>();
		// dohvati slične filmove

		for (String likedMovieName : user.getLikedMovieNames()) {
			Movie likedMovie = movieDao.findMovieByName(likedMovieName);
			if (likedMovie == null) {
				System.out.println("Nema filma za naslov:" + likedMovieName);
				continue;
			}
			Set<RecommendationRecord> similarAsRecords = new HashSet<>();

			for (Movie sim : likedMovie.getSimilarMovies()) {
				similarAsRecords.add(new RecommendationRecord(sim, sim
						.getTmdbInfo().getVoteAverage()));
			}
			similarsForLikedMovies.put(likedMovie, similarAsRecords);
		}

		Set<String> likedMovieNames = user.getLikedMovieNames();
		// napravi jedan set u kojem su svi filmovi koji kandidiraju
		Set<RecommendationRecord> allSimilars = new HashSet<>();
		for (Set<RecommendationRecord> value : similarsForLikedMovies.values()) {
			for (RecommendationRecord val : value) {
				if (likedMovieNames.contains(val.getMovie().getTitle())) {
					continue;
				} else {
					allSimilars.add(val);
				}
			}
		}

		// za svaki film koji kandidira , provjeri dal se pojavljuje vise puta!
		for (RecommendationRecord similar : allSimilars) {
			for (Set<RecommendationRecord> value : similarsForLikedMovies
					.values()) {
				if (value.contains(similar)) {
					similar.setRecScore(similar.getRecScore()
							+ similar.getMovie().getTmdbInfo().getVoteAverage());
				}
			}
		}
		
		Set<RecommendationRecord> setWithFriends = friendsService.getRecordsWtihFriendLikes(allSimilars, user);
		
		List<RecommendationRecord> finalRec = new LinkedList<>(allSimilars);
		Collections.sort(finalRec);

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
		for (RecommendationRecord recommendationRecord : finalRec) {
			System.out.println(recommendationRecord.getMovie().getTitle()
					+ "\t" + recommendationRecord.getRecScore());
		}

		return finalRec;
	}
}
