package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieRecommendationImpl implements MovieRecommendation {

	@Override
	public List<Movie> calculateRecommendation(ApplicationUser user) {
		Map<Movie, List<Movie>> recomForLikedMovies = new HashMap<>();

		for (String likedMovieName : user.getLikedMovieNames()) {
			Movie likedMovie;
		}

		return null;
	}

}
