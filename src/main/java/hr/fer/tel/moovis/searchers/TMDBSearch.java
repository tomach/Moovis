package hr.fer.tel.moovis.searchers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.MovieDb;
import com.omertron.themoviedbapi.model.PersonCast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippm on 10.11.14..
 */
public class TMDBSearch extends GenericSearch {

    private static final String API_KEY = "5086b39f4b96483187ec864955e4da88";
    private static final String MY_QUEUE = "THDBSearchQueue";

    private TheMovieDbApi theMovieDbApi;

    public TMDBSearch() throws MovieDbException, UnknownHostException {
        super();
        theMovieDbApi = new TheMovieDbApi(API_KEY);

    }


    @Override
    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {
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

            BasicDBObject movieDetails = new BasicDBObject()
                    .append("id", movie.getId())
                    .append("title", movie.getTitle()).append("budget", movie.getBudget())
                    .append("popularity", movie.getPopularity()).append("userRating", movie.getUserRating())
                    .append("voteAverage", movie.getVoteAverage()).append("homepage", movie.getHomepage())
                    .append("revenue", movie.getRevenue()).append("releaseDate", movie.getReleaseDate())
                    .append("voteCount", movie.getVoteCount());

            if (movie.getGenres() != null) {
                List<String> genres = new ArrayList<String>();
                for (Genre genre : movie.getGenres()) {
                    genres.add(genre.getName());
                }
                movieDetails.append("genres", genres);
            }
            try {
                if (movie.getCast() != null) {

                    List<BasicDBObject> cast = new ArrayList<BasicDBObject>();
                    for (PersonCast pers : movie.getCast()) {
                        cast.add(new BasicDBObject().append("id", pers.getId()).append("castId", pers.getCastId()).append("name", pers.getName()).append("character", pers.getCharacter()));
                    }
                    movieDetails.append("cast", cast);
                }
            } catch (Exception e) {
            }

            newMovieObject.append("tmdb", movieDetails);
        } catch (MovieDbException e) {
        }
    }

    @Override
    protected DBCollection getQueue(DB db) {
        return db.getCollection(MY_QUEUE);
    }

    public static void main(String[] args) throws MovieDbException, UnknownHostException {
        new Thread(new TMDBSearch()).start();
    }
}
