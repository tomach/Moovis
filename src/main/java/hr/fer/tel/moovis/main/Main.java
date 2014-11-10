package hr.fer.tel.moovis.main;

import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbReview;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.themoviedbapi.MovieDbException;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import hr.fer.tel.moovis.apis.FacebookAPI;
import hr.fer.tel.moovis.searchers.IMDBSearcher;
import hr.fer.tel.moovis.searchers.RottenTomatoesSearch;
import hr.fer.tel.moovis.searchers.TMDBImpl;
import hr.fer.tel.moovis.searchers.YouTubeSearch;

import java.net.UnknownHostException;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class Main {

    public static void main(String[] args) throws UnknownHostException, MovieDbException, RottenTomatoesException {

        new Thread(new IMDBSearcher()).start();
        new Thread(new TMDBImpl()).start();
        new Thread(new YouTubeSearch()).start();
        new Thread(new RottenTomatoesSearch()).start();
    }
}