package hr.fer.tel.moovis.model.movie;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.Person;

public class MovieProxy extends Movie {

	private static MovieDao movieDao;
	static {
		try {
			movieDao = new MovieDao();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MovieProxy() {
		super();
	}

	public MovieProxy(String title, YouTubeInfo ytInfo, IMDBMovieInfo imdbInfo,
			TMDBMovieInfo tmdbInfo, List<String> genres, List<Person> cast,
			List<Person> directors, List<Movie> similarMovies) {
		super(title, ytInfo, imdbInfo, tmdbInfo, genres, cast, directors,
				similarMovies);
	}

	@Override
	public List<Movie> getSimilarMovies() {
		if (super.getSimilarMovies() == null) {
			List<String> simNames = movieDao.getSimilarMoviesNames(super
					.getTitle());
			if (simNames != null) {
				List<Movie> similars = new ArrayList<Movie>();
				for (String name : simNames) {
					Movie simMovieObj = movieDao.findMovieByName(name);
					if (simMovieObj != null) {
						similars.add(simMovieObj);
					}
				}
				super.setSimilarMovies(similars);
			} else {
				super.setSimilarMovies(new ArrayList<Movie>());
			}
		}
		return super.getSimilarMovies();
	}

	@Override
	public String toString() {
		return "MovieProxy" + super.toString();
	}
}
