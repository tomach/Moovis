package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRecommendationImpl implements MovieRecommendation {
	@Autowired
	private MovieDao movieDao;

	@Override
	public List<Movie> calculateRecommendation(ApplicationUser user) {
		Map<Movie, List<Movie>> recomForLikedMovies = new HashMap<>();

		for (String likedMovieName : user.getLikedMovieNames()) {
			Movie likedMovie = movieDao.findMovieByName(likedMovieName);
			System.out.println(likedMovieName);
			System.out.println(likedMovie);
		}

		return null;
	}

}
