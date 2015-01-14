package hr.fer.tel.moovis.model.movie;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

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
		// TODO Auto-generated constructor stub
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
					similars.add(movieDao.findMovieByName(name));
				}
				super.setSimilarMovies(similars);
			}
		}
		return super.getSimilarMovies();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
