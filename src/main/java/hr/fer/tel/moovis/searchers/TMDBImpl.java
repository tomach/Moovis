package hr.fer.tel.moovis.searchers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;

import java.net.UnknownHostException;

/**
 * Created by filippm on 10.11.14..
 */
public class TMDBImpl extends GenericSearch {

    private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";
    private static final int SLEEP_TIME = 100 * 60;
    private static final String MY_QUEUE = "THDBSearchQueue";

    private TheMovieDbApi theMovieDbApi;

    public TMDBImpl() throws MovieDbException, UnknownHostException {
        super();
        theMovieDbApi = new TheMovieDbApi(API_KEY);

    }


    @Override
    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
        String movieKey = obj.get("movieKey").toString();
        String movieName = obj.get("name").toString();
        String movieYear = obj.get("year").toString();
        System.out.println(movieKey);
        System.out.println(movieName);
        System.out.println(movieYear);

        //Obrada uz TMDB api
        try {
            MovieDb movie = theMovieDbApi.searchMovie(movieName, Integer.parseInt(movieYear), null, false, 0).getResults().get(0);
            System.out.println(movie);
            DBObject movieDetails = new BasicDBObject().append("tmdbid", movie.getId());
            newMovieObject.append("tmdb", movieDetails);
        } catch (MovieDbException e) {

        }
    }

    @Override
    protected DBCollection getQueue(DB db) {
        return db.getCollection(MY_QUEUE);


    }

    public static void main(String[] args) throws MovieDbException, UnknownHostException {
        new Thread(new TMDBImpl()).start();
    }
}
