package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.MovieDb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislaf on 10.11.2014..
 */
public class IMDBSearcher extends GenericSearch {
    public static final String IMDB_SEARCH_QUEUE = "IMDBSearchQueue";

    public IMDBSearcher() throws UnknownHostException {
        super();
    }

    @Override
    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
        String movieKey = obj.get("movieKey").toString();
        String imdbId = obj.get("imdbId").toString();

        System.out.println(movieKey);
        System.out.println(imdbId);
        //IMDB dohvat
        ImdbMovieDetails movie = ImdbApi.getFullDetails(imdbId);
        System.out.println(movie);
        DBObject movieDetails = new BasicDBObject().append("imdbId", movie.getImdbId()).append("title", movie.getTitle()).append("rating", movie.getRating()).append("summary", movie.getBestPlot().getSummary());
        newMovieObject.append("imdb", movieDetails);

    }

    @Override
    protected DBCollection getQueue(DB db) {
        return db.getCollection(IMDB_SEARCH_QUEUE);
    }
}
