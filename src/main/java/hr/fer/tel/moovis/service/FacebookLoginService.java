package hr.fer.tel.moovis.service;

import hr.fer.tel.moovis.apis.FacebookAPI;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import facebook4j.FacebookException;
import facebook4j.Friend;
import facebook4j.Movie;
import facebook4j.User;
import facebook4j.internal.org.json.JSONException;

public class FacebookLoginService {

	public static void getUserLoginData(String accessToken) {

		try {

			FacebookAPI faceApi = new FacebookAPI(accessToken);
			
			User user = faceApi.getUser();
			String facebookId = user.getId();
			String name = user.getFirstName();
			String surname = user.getLastName();
			//List<Movie> movies = faceApi.getMovies(0);
			//List<Friend> friends = faceApi.getFriends();
			
			List<String> movieNames = getAllMovieNames(faceApi.getMovies(0));
			List<String> friendIds = getAllFacebookIds(faceApi.getFriends());
			
			//TODO add call to dao save

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FacebookException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private static List<String> getAllMovieNames(List<Movie> movies) {
		List<String> movieNames = new ArrayList<String>();
		for (Movie movie : movies) {
			movieNames.add(movie.getName());
		}
		return movieNames;
	}
	
	private static List<String> getAllFacebookIds(List<Friend> friends) {
		List<String> facebookIds = new ArrayList<String>();
		for (Friend friend : friends) {
			facebookIds.add(friend.getId());
		}
		return facebookIds;
	}
}
