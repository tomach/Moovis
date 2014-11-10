package hr.fer.tel.moovis.searchers;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by filippm on 10.11.14..
 */
public abstract class GenericSearch implements Runnable {
    private static final int SLEEP_TIME = 100 * 60;
    private static final String DB_NAME = "moovis";

    private MongoClient mongo;
    private DB db;
    private DBCollection movies;

    public GenericSearch() throws UnknownHostException {
        // Since 2.10.0, uses MongoClient
        mongo = new MongoClient("localhost", 27017);
        db = mongo.getDB(DB_NAME);

        movies = db.getCollection("movies");
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
        DBCollection queue = getQueue(db);
        //bzbz
        DBCursor cursor = queue.find();
        DBObject obj;

        try {
            while (cursor.hasNext()) {
                //Dohvati iz reda
                obj = cursor.next();
                queue.remove(obj);
                System.out.println(obj);
                String movieKey = obj.get("movieKey").toString();

                //Obrada uz TMDB api

                //spremanje u bazu
                DBObject searchOldMovie = new BasicDBObject().append("movieKey", movieKey);
                System.out.println("\t\t" + searchOldMovie);

                Cursor oldMovieCursor = movies.find(searchOldMovie);
                if (oldMovieCursor.hasNext()) {
                    BasicDBObject oldMovieObject = (BasicDBObject) oldMovieCursor.next();
                    BasicDBObject newMovieObject = (BasicDBObject) oldMovieObject.copy();
                    processMovie(obj, newMovieObject);
                    movies.update(oldMovieObject, newMovieObject);
                }
                oldMovieCursor.close();

            }
        } finally {
            cursor.close();
        }

    }

    protected abstract void processMovie(DBObject obj, BasicDBObject newMovieObject);


    protected abstract DBCollection getQueue(DB db);
}
