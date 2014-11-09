package hr.fer.tel.moovis.searchers;

import com.mongodb.MongoClient;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;

import java.net.UnknownHostException;

/**
 * Created by filippm on 09.11.14..
 */
public class TMDBSearch implements Runnable {

    private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";
    private static final int SLEEP_TIME = 100 * 60;
    private TheMovieDbApi dbApi;
    private MongoClient mongo;

    public TMDBSearch() throws MovieDbException, UnknownHostException {
        dbApi = new TheMovieDbApi(API_KEY);

        // Since 2.10.0, uses MongoClient
        mongo = new MongoClient("localhost", 27017);
    }


    public void run() {
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


    }
}
