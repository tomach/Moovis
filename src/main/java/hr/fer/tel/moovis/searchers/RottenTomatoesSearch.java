package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.omertron.rottentomatoesapi.RottenTomatoesApi;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.RTMovie;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Damjan on 9.11.2014..
 */
public class RottenTomatoesSearch implements Runnable {

    private static final String API_KEY = "sdsque4c4t6gynrtv8znmrrz";
    private static final String SERVER = "localhost";
    private static final String DATABASE_NAME = "moovis";
    private static final String ROTTEN_COLLECTION = "RottenSearchQueue";
    private static final String MOVIE_KEY = "movieKey";
    private static final String MOVIE_NAME = "name";
    //private static final String MOVIE_YEAR = "year";
    private static final int PORT = 27017;
    private static final int SLEEP_TIME = 100 * 60;

    private RottenTomatoesApi rottenApi;
    //private MongoClient mongoClient;
    //private DB mongoDB;
    private DBCollection rottenSearchQueue;


    public RottenTomatoesSearch() throws RottenTomatoesException, UnknownHostException {

        rottenApi = new RottenTomatoesApi(API_KEY);
        MongoClient mongoClient = new MongoClient(SERVER, PORT);
        DB mongoDB = mongoClient.getDB(DATABASE_NAME);
        rottenSearchQueue = mongoDB.getCollection(ROTTEN_COLLECTION);
        addMockEntry();
    }

    private void addMockEntry() {

        DBObject dbObject = new BasicDBObject().append(MOVIE_KEY, "Gone Girl2014").append(MOVIE_NAME, "Gone Girl");
        rottenSearchQueue.insert(dbObject);

        dbObject = new BasicDBObject().append(MOVIE_KEY, "The Edge of Tommorow2014")
                .append(MOVIE_NAME, "The Edge of Tommorow");

        rottenSearchQueue.insert(dbObject);
    }


    public void run() {

        while (true) {

            //execute main job
            searchProcess();

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void searchProcess() {

        DBCursor dbCursor = rottenSearchQueue.find();
        String movieKey;
        String movieName;
        //String movieYear;


        while (dbCursor.hasNext()) {

            DBObject dbObject = dbCursor.next();

            movieKey = dbObject.get(MOVIE_KEY).toString();
            movieName = dbObject.get(MOVIE_NAME).toString();
        //    movieYear = dbObject.get(MOVIE_YEAR).toString();

            rottenSearchQueue.remove(dbObject);

        //    System.out.println(movieKey);
        //    System.out.println(movieName);
        //    System.out.println(movieYear);

            List<RTMovie> movieList = searchMoviesOnRotten(movieName);

            if (null != movieList) {

                for (RTMovie movie : movieList) {
                    System.out.println("Movie ID: " + movie.getId());
                    System.out.println("Movie title" + movie.getTitle());
                    System.out.println("Movie year: " +movie.getYear());
                }
            }
        }


    }


    private List<RTMovie> searchMoviesOnRotten(String movieName) {

        List<RTMovie> movieList = null;

        try {

            movieList = rottenApi.getMoviesSearch(movieName);


        } catch (RottenTomatoesException e) {
            e.printStackTrace();
        }

        return movieList;
    }


    public static void main(String[] args) throws Exception {

        new Thread(new RottenTomatoesSearch()).start();
    }
}
