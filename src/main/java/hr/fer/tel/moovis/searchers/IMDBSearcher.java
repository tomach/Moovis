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
    }


    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
        String movieKey = obj.get("movieKey").toString();
        String imdbId = obj.get("imdbId").toString();

        System.out.println(movieKey);
        System.out.println(imdbId);

        //IMDB dohvat
        ImdbMovieDetails movie = ImdbApi.getFullDetails(imdbId);
        System.out.println("IMDB search:" + movie);



        DBObject plot = new BasicDBObject()
                .append("outline", movie.getBestPlot().getOutline())
                .append("summary", movie.getBestPlot().getSummary())
                .append("author", movie.getBestPlot().getAuthor())
                .append("text", movie.getBestPlot().getText());
        BasicDBList cast = new BasicDBList();
        for (ImdbCast ca : movie.getCast()) {
            cast.add(new BasicDBObject().append("id", ca.getPerson().getActorId()).append("name", ca.getPerson().getName()));
        }

        BasicDBList genres = new BasicDBList();
        for (String g : movie.getGenres()) {
            genres.add(g);
        }
        DBObject movieDetails = new BasicDBObject().append("imdbId", movie.getImdbId())
                .append("title", movie.getTitle())
                .append("rating", movie.getRating())
                .append("year", movie.getYear())
                .append("cast", cast)
                .append("plot", plot)
                .append("genres", genres);
        newMovieObject.append("imdb", movieDetails);

    }

    @Override
    protected DBCollection getQueue(DB db) {
        return db.getCollection(IMDB_SEARCH_QUEUE);
    }

    @Override
    protected long getSleepTime() {
        return 0;
    }

    public static void main(String[] argv) throws UnknownHostException {
        new Thread(new IMDBSearcher()).start();
    }
}
