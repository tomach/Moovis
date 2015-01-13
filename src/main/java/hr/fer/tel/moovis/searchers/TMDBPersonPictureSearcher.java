package hr.fer.tel.moovis.searchers;

import java.net.UnknownHostException;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Artwork;
import com.omertron.themoviedbapi.model.Person;
import com.omertron.themoviedbapi.results.TmdbResultsList;

public class TMDBPersonPictureSearcher {

	private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";

	private TheMovieDbApi theMovieDbApi;

	public TMDBPersonPictureSearcher() throws MovieDbException,
			UnknownHostException {
		super();
		theMovieDbApi = new TheMovieDbApi(API_KEY);
	}

	public void process() throws MovieDbException {
		Person person = theMovieDbApi.getPersonInfo(19274, "profilePath");
		System.out.println(person);
	}

	public static void main(String[] args) throws UnknownHostException,
			MovieDbException {
		new TMDBPersonPictureSearcher().process();
	}
}
