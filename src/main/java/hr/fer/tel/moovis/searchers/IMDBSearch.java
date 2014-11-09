package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislaf on 9.11.2014..
 */
public class IMDBSearch implements Runnable {

    private MongoClient mongo;
    private DB dbMoovis;
    private DBCollection imdbSearchQueue;
    private DBCollection movies;
    private static int SLEEP_TIME = 100 * 60;

    public IMDBSearch() throws UnknownHostException {
        mongo = new MongoClient("localhost", 10);
        dbMoovis = mongo.getDB("moovis");
        imdbSearchQueue = dbMoovis.getCollection("IMDBSearchQueue");
        movies = dbMoovis.getCollection("movies");
    }
    @Override
    public void run() {
        while(true){
            searchProcess();
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchProcess(){
        List<DBObject> objects = new ArrayList<DBObject>();
        Cursor c = imdbSearchQueue.find();
        while (c.hasNext()){
            DBObject ob = c.next();
            objects.add(ob);
            imdbSearchQueue.remove(ob);
        }

        if (objects.size() > 0){
            for (DBObject ob : objects) {
                String imdbId = (String) ob.get("imdbId");
                String key = (String) ob.get("movieKey");
                ImdbMovieDetails movie = ImdbApi.getFullDetails(imdbId);
                DBObject imdbMovieDetails = new BasicDBObject().append("imdbId", movie.getImdbId()).append("title", movie.getTitle()).append("rating", movie.getRating());
                DBObject imdbMovie = new BasicDBObject().append("imdb", imdbMovieDetails);
                DBObject searchOldMovie = new BasicDBObject().append("movieKey", key);
                Cursor oldMovieCursor = movies.find(searchOldMovie);
                if (oldMovieCursor.hasNext()){
                    BasicDBObject oldMovieObject = (BasicDBObject) oldMovieCursor.next();
                    BasicDBObject newMovieObject = (BasicDBObject) oldMovieObject.copy();
                    newMovieObject.append("imdb", imdbMovie);
                    movies.update(oldMovieObject, newMovieObject, true, true);
                }
            }
        }
    }
}
