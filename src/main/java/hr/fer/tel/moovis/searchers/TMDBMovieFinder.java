package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;

import java.net.UnknownHostException;

/**
 * Created by filippm on 10.11.14..
 */
public class TMDBMovieFinder implements Runnable {

    private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";

    private static final String DB_NAME = "moovis";

    private MongoClient mongo;
    private DB db;
    private DBCollection movies;
    private DBCollection rottenSearchQueue;
    private DBCollection tmdbSearchQueue;
    private DBCollection ytbSearchQueue;


    private TheMovieDbApi theMovieDbApi;

    public TMDBMovieFinder() throws UnknownHostException, MovieDbException {
        theMovieDbApi = new TheMovieDbApi(API_KEY);

        // Since 2.10.0, uses MongoClient
        mongo = new MongoClient("localhost", 27017);
        db = mongo.getDB(DB_NAME);

        movies = db.getCollection("movies");


        rottenSearchQueue = db.getCollection("RottenSearchQueue");
        tmdbSearchQueue = db.getCollection("TMDBSearchQueue");
        ytbSearchQueue = db.getCollection("YTSearchQueue");

    }

    public void run() {
        for (int counter = 0; ; counter++) {
            try {
                Thread.sleep(330);
            } catch (InterruptedException e) {
            }
            try {

                MovieDb movieInfo = theMovieDbApi.getMovieInfo(counter, null);
                if (movieInfo != null) {
                    DBObject dbMovie = new BasicDBObject().append("movieKey", movieInfo.getOriginalTitle());
                    Cursor checkExists = movies.find(dbMovie);

                    if (!checkExists.hasNext()) {
                        System.out.println(movieInfo.getId() + ";" + movieInfo.getOriginalTitle());
                        ytbSearchQueue.insert(dbMovie);
                        rottenSearchQueue.insert(dbMovie);
                        tmdbSearchQueue.insert(dbMovie);
                    }
                }
            } catch (MovieDbException e) {
                System.err.println("Counter:" + counter + " fails.");
            }
        }
    }


    public static void main(String[] args) throws MovieDbException, UnknownHostException {
        new Thread(new TMDBMovieFinder()).start();
    }
}
