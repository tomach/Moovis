package hr.fer.tel.moovis.searchers;

import com.mongodb.*;

import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbCast;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;

import java.net.UnknownHostException;


/**
 * Created by tomislaf on 10.11.2014..
 */
public class IMDBSearcher extends GenericSearch {
    public static final String IMDB_SEARCH_QUEUE = "IMDBSearchQueue";

    public IMDBSearcher() throws UnknownHostException {
        super();
        DBObject obj = new BasicDBObject().append("movieKey", "goneGirlSmreki").append("imdbId", "tt2267998");
        MongoClient mongo;
        DB dbMoovis;
        DBCollection imdbSearchQueue;
        mongo = new MongoClient("localhost", 27017);
        dbMoovis = mongo.getDB("moovis");
        imdbSearchQueue = dbMoovis.getCollection("IMDBSearchQueue");
        imdbSearchQueue.insert(obj);
    }


    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
        String movieKey = obj.get("movieKey").toString();
        String imdbId = obj.get("imdbId").toString();

        System.out.println(movieKey);
        System.out.println(imdbId);

        //IMDB dohvat
        ImdbMovieDetails movie = ImdbApi.getFullDetails(imdbId);
        System.out.println(movie);

        DBObject plot = new BasicDBObject().append("summary", movie.getPlot().getSummary())
                .append("author", movie.getPlot().getAuthor()).append("text", movie.getPlot().getText());
        BasicDBList cast = new BasicDBList();
        for (ImdbCast ca : movie.getCast()){
            cast.add(ca.getPerson().getName());
        }

        DBObject movieDetails = new BasicDBObject().append("imdbId", movie.getImdbId())
                .append("title", movie.getTitle())
                .append("rating", movie.getRating())
                .append("year", movie.getYear())
                .append("cast", cast)
                .append("plot", plot);

        newMovieObject.append("imdb", movieDetails);

    }

    @Override
    protected DBCollection getQueue(DB db) {
        return db.getCollection(IMDB_SEARCH_QUEUE);
    }

    public static void main(String[]argv) throws UnknownHostException {
        new Thread(new IMDBSearcher()).start();
    }
}
