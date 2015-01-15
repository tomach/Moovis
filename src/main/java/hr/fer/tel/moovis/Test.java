package hr.fer.tel.moovis;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;

public class Test {
	private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";

	public static void main(String[] args) throws UnknownHostException,
			MovieDbException {
		// TODO Auto-generated method stub
		TheMovieDbApi theMovieDbApi = new TheMovieDbApi(API_KEY);

		System.out.println(theMovieDbApi.getMovieImages(2348, null, "")
				.getResults().get(0).getFilePath());

	}
}
