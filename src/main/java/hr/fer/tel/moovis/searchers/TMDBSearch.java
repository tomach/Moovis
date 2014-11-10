package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;

import java.net.UnknownHostException;

/**
 * Created by filippm on 09.11.14..
 */
public class TMDBSearch implements Runnable {
    private static final String DB_NAME = "moovis";
    private static final String MY_QUEUE = "THDBSearchQueue";
    private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";
    private static final int SLEEP_TIME = 100 * 60;
    private TheMovieDbApi theMovieDbApi;
    private MongoClient mongo;

    public TMDBSearch() throws MovieDbException, UnknownHostException {
        theMovieDbApi = new TheMovieDbApi(API_KEY);

        // Since 2.10.0, uses MongoClient
        mongo = new MongoClient("localhost", 27017);
    }


    public void run() {
        System.out.println(Thread.currentThread().getId());

        while (true) {

            searchProcess();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchProcess() {
        DB db = mongo.getDB(DB_NAME);
        DBCollection table = db.getCollection(MY_QUEUE);
        //bzbz
        DBCursor cursor = table.find();
        DBObject obj;

        try {
            while (cursor.hasNext()) {
                //Dohvati iz reda
                obj = cursor.next();
                table.remove(obj);
                System.out.println(obj);
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
                } catch (MovieDbException e) {
                    continue;
                }
            }
        } finally {
            cursor.close();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getId());
        new Thread(new TMDBSearch()).start();
    }
}
