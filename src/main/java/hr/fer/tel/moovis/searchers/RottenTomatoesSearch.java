package hr.fer.tel.moovis.searchers;

import com.mongodb.*;
import com.omertron.rottentomatoesapi.RottenTomatoesApi;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTMovie;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * Created by Damjan on 10.11.2014..
 */
public class RottenTomatoesSearch extends GenericSearch {

    private static final String API_KEY = "sdsque4c4t6gynrtv8znmrrz";
    private static final String ROTTEN_COLLECTION = "RottenSearchQueue";

    private static final String MOVIE_KEY = "movieKey";
    private static final String MOVIE_NAME = "name";
    private static final String ROTTEN_MOVIE_ID = "rottenid";
    private static final String ROTTEN_MOVIE_TITLE = "rottentitle";
    private static final String ROTTEN_MOVIE_YEAR = "rottenyear";
    private static final String ROTTEN_MOVIE_CRITICS = "rottencritics";
    private static final String ROTTEN_MOVIE_CAST = "rottencast";
    private static final String ROTTEN_MOVIE_GENRES = "rottengenres";


    private RottenTomatoesApi rottenApi;


    public RottenTomatoesSearch() throws UnknownHostException, RottenTomatoesException {
        super();
        rottenApi = new RottenTomatoesApi(API_KEY);
    }

    @Override
    protected void processMovie(DBObject obj, BasicDBObject newMovieObject) {

        String movieKey = obj.get(MOVIE_KEY).toString();
        String movieName = obj.get(MOVIE_NAME).toString();
        //String movieYear = dbObject.get(MOVIE_YEAR).toString();

        System.out.println(movieKey);
        System.out.println(movieName);
        //System.out.println(movieYear);

        List<RTMovie> movieList = searchMoviesOnRotten(movieName);

        if (null != movieList) {
            if (movieList.size() > 0) {
                RTMovie movie = movieList.get(0);

                //fill IMDBQueue
                fillIMDBQueue(movieKey, movie);

                DBObject movieDetails = setMovieData(movie);
                newMovieObject.append("rotten", movieDetails);
            }
        }
    }

    private void fillIMDBQueue(String movieKey, RTMovie movie) {

        System.out.println(movie.getAlternateIds().toString());

        if (movie.getAlternateIds().containsKey("imdb")) {

            DB db = getDb();

            BasicDBObject imdbObject = new BasicDBObject()
                    .append(MOVIE_KEY, movieKey)
                    .append("imdbId", "tt" + movie.getAlternateIds().get("imdb"));

            DBCollection table = db.getCollection("IMDBSearchQueue");
            table.insert(imdbObject);
            System.out.println("IMDB queue filled");
        }
    }


    @Override
    protected DBCollection getQueue(DB db) {

        return db.getCollection(ROTTEN_COLLECTION);
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

    private DBObject setMovieData(RTMovie movie) {

        Set<RTCast> castList = movie.getCast();
        BasicDBList cast = new BasicDBList();

        if (null != castList) {
            for (RTCast castPerson : castList) {
                cast.add(castPerson.getCastName());
            }
        }

        Set<String> genresList = movie.getGenres();
        BasicDBList genres = new BasicDBList();

        if (null != genresList) {
            for (String genre : genresList) {
                genres.add(genre);
            }
        }

        //TODO directors, ratings i popravi ovo gore
        return new BasicDBObject()
                .append(ROTTEN_MOVIE_ID, movie.getId())
                .append(ROTTEN_MOVIE_TITLE, movie.getTitle())
                .append(ROTTEN_MOVIE_YEAR, movie.getYear())
                .append(ROTTEN_MOVIE_CRITICS, movie.getCriticsConsensus());
        //       .append(ROTTEN_MOVIE_CAST, castList)
        //       .append(ROTTEN_MOVIE_GENRES, genres);
    }

    public static void main(String[] args) throws Exception {

        new Thread(new RottenTomatoesSearch()).start();
    }
}
