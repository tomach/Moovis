package hr.fer.tel.moovis.apis;

import com.mongodb.*;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONException;
import hr.fer.tel.moovis.searchers.IMDBSearcher;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class FacebookAPI {

	private static final String API_KEY = "538301972979920";
	private static final String APP_SECRET = "8a17d41e0334cda6f52af4b6481f92fa";
	private static final String CLIENT_TOKEN = "CAAHplTHhoNABAEvZAeWfvNK5FdFOIvXpxdPiMy3EmHAtWeNqPHjmH3YR6ibL1UZC8jsHvGUJVAr5tP5XnA1lN7ZAopKyCta1soZC2ZCNnNDjEOAY6f2f73RAKE9djv9N7v8MwDwdZAeTKsoH8w3VTYZCYEVsrCahPavt4QvY5lsswY0ZAnRopCyE5YIEZCIipecFdArv2eN17hm10SVwHGIpy";
	private static final String ACC_TOKEN = "CAAHplTHhoNABABA1FpWPoQN1yjYBZAezVZCfmCMlZALthsatjZCR9qwdE0UlnedknXYeQSfE393MdvgZB6bfqh8KhoIbcvLdBIefOZBut0JEvrjNsZB8ofH5HeeDDkgpeAjA9GQ5qN9qtipqRLi4fCLMG2VT0Nl1lz4mHY7NUgYFB9hzleAyQQ42gHgi3b0lqW0GtULDeCR6JuZApWuA719A";
	private Facebook facebook;

	public FacebookAPI(String accessToken) throws UnknownHostException {
		facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(API_KEY, APP_SECRET);
		facebook.setOAuthPermissions("user_likes");
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
	}

	public Facebook getFacebook() {
		return facebook;
	}

	public User getUser() throws FacebookException, UnknownHostException,
			JSONException {
		Reading r = new Reading();
		r.fields("id", "name", "first_name", "last_name");
		return facebook.getMe(r);
	}

	public static void main(String argv[]) throws UnknownHostException {
		FacebookAPI fa = new FacebookAPI(ACC_TOKEN);
		try {
			System.out.println(fa.getMovies(0).size());
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Movie> getMovies(long sinceTimestamp) throws FacebookException {
		Date since = new Date(sinceTimestamp);

		ResponseList<Movie> likedMovies = facebook.getMovies();
		List<Movie> result = new ArrayList<>();

		int totalNum = likedMovies.size();
		for (Movie fbM : likedMovies) {
			if (fbM.getCreatedTime().before(since))
				break;
			result.add(fbM);
		}

		Paging<Movie> moviePaging = likedMovies.getPaging();
		while ((likedMovies = facebook.fetchNext(moviePaging)) != null) {
			totalNum += likedMovies.size();
			boolean stop = false;
			for (Movie fbM : likedMovies) {
				if (fbM.getCreatedTime().before(since)) {
					stop = true;
					break;
				}
				result.add(fbM);
			}
			if (stop)
				break;
			moviePaging = likedMovies.getPaging();
		}

		return result;
	}

	public List<Friend> getFriends() throws FacebookException {
		ResponseList<Friend> friends = facebook.getFriends();
		List<Friend> result = new ArrayList<>();

		int totalNum = friends.size();
		for (Friend fbM : friends) {
			result.add(fbM);
		}

		Paging<Friend> friendPaging = friends.getPaging();
		while ((friends = facebook.fetchNext(friendPaging)) != null) {
			totalNum += friends.size();
			for (Friend fbM : friends) {
				result.add(fbM);
			}
			friendPaging = friends.getPaging();
		}
		return result;
	}

	// za damsa
	public void spremanjeFilmova() throws UnknownHostException,
			FacebookException {
		MongoClient mongo;
		DB dbMoovis;
		DBCollection rottenSearchQueue;
		DBCollection tmdbSearchQueue;
		DBCollection ytbSearchQueue;

		DBCollection movies;

		mongo = new MongoClient("localhost", 27017);
		dbMoovis = mongo.getDB("moovis");
		rottenSearchQueue = dbMoovis.getCollection("RottenSearchQueue");
		tmdbSearchQueue = dbMoovis.getCollection("TMDBSearchQueue");
		ytbSearchQueue = dbMoovis.getCollection("YTSearchQueue");
		movies = dbMoovis.getCollection("movies");

		ResponseList<Movie> likedMovies = facebook.getMovies();

		int totalNum = likedMovies.size();
		for (Movie fbM : likedMovies) {
			DBObject dbMovie = new BasicDBObject().append("movieKey",
					fbM.getName()).append("name", fbM.getName());
			Cursor checkExists = movies.find(dbMovie);
			if (!checkExists.hasNext()) {
				ytbSearchQueue.insert(dbMovie);
				rottenSearchQueue.insert(dbMovie);
				tmdbSearchQueue.insert(dbMovie);
			}
			checkExists.close();
		}

		Paging<Movie> moviePaging = likedMovies.getPaging();
		while ((likedMovies = facebook.fetchNext(moviePaging)) != null) {
			totalNum += likedMovies.size();
			System.out.println("fb liked movies " + likedMovies);
			for (Movie fbM : likedMovies) {
				DBObject dbMovie = new BasicDBObject().append("movieKey",
						fbM.getName()).append("name", fbM.getName());
				Cursor checkExists = movies.find(dbMovie);
				if (!checkExists.hasNext()) {
					ytbSearchQueue.insert(dbMovie);
					rottenSearchQueue.insert(dbMovie);
					tmdbSearchQueue.insert(dbMovie);
				}
				checkExists.close();
			}
			moviePaging = likedMovies.getPaging();
		}
		System.out.println("broj dohvacenih filmova " + totalNum);
	}

}
