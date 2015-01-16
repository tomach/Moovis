package hr.fer.tel.moovis;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;

public class Test {

	public static void main(String[] args) throws MovieDbException {

		TheMovieDbApi theMovieDbApi = new TheMovieDbApi(
				"5086b39f4b96483187ec864955e4da88");
		int id = theMovieDbApi.searchPeople("Evan Goldberg", true, 0)
				.getResults().get(0).getId();

		System.out.println(theMovieDbApi.getPersonInfo(id, ""));

	}
}
