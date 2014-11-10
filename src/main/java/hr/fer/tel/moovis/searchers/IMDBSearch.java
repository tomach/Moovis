package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbCast;
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
        mongo = new MongoClient("localhost", 27017);
        dbMoovis = mongo.getDB("moovis");
        imdbSearchQueue = dbMoovis.getCollection("IMDBSearchQueue");
        movies = dbMoovis.getCollection("movies");
        DBObject objM = new BasicDBObject().append("movieKey", "goneGirlSmreki");
        movies.remove(objM);
        movies.insert(objM);
        Cursor beforeM = movies.find();
        while (beforeM.hasNext()) {
            System.out.println("Before movies " + beforeM.next());
        }
        DBObject obj = new BasicDBObject().append("movieKey", "goneGirlSmreki").append("imdbId", "tt2267998");
        imdbSearchQueue.insert(obj);
        beforeM.close();
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
        List<DBObject> objects = new ArrayList<DBObject>();
        Cursor c = imdbSearchQueue.find();
        while (c.hasNext()) {
            DBObject ob = c.next();
            objects.add(ob);
            imdbSearchQueue.remove(ob);
        }
        c.close();

        if (objects.size() > 0) {
            for (DBObject ob : objects) {
                System.out.println("IMDBSearchQueue " + ob);
                String imdbId = (String) ob.get("imdbId");
                String key = (String) ob.get("movieKey");
                ImdbMovieDetails movie = ImdbApi.getFullDetails(imdbId);
                System.out.println("imdbMovie found " + movie);
                DBObject plot = new BasicDBObject().append("summary", movie.getPlot().getSummary())
                        .append("author", movie.getPlot().getAuthor()).append("text", movie.getPlot().getText());
                BasicDBList cast = new BasicDBList();
                for (ImdbCast ca : movie.getCast()){
                    cast.add(ca.getPerson().getName());
                }

                DBObject imdbMovieDetails = new BasicDBObject().append("imdbId", movie.getImdbId())
                        .append("title", movie.getTitle())
                        .append("rating", movie.getRating())
                        .append("cast", cast)
                        .append("plot", plot);

                DBObject searchOldMovie = new BasicDBObject().append("movieKey", key);
                Cursor oldMovieCursor = movies.find(searchOldMovie);
                if (oldMovieCursor.hasNext()) {
                    BasicDBObject oldMovieObject = (BasicDBObject) oldMovieCursor.next();
                    BasicDBObject newMovieObject = (BasicDBObject) oldMovieObject.copy();
                    newMovieObject.append("imdb", imdbMovieDetails);
                    movies.update(oldMovieObject, newMovieObject);
                }
                oldMovieCursor.close();
                Cursor movisC = movies.find();
                while (movisC.hasNext()) {
                    System.out.println("After movies " + movisC.next());
                }
                movisC.close();
            }
        }
    }
}
