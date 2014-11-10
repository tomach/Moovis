package hr.fer.tel.moovis.main;

import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.themoviedbapi.MovieDbException;
import hr.fer.tel.moovis.searchers.IMDBSearcher;
import hr.fer.tel.moovis.searchers.RottenTomatoesImpl;
import hr.fer.tel.moovis.searchers.TMDBSearch;
import hr.fer.tel.moovis.searchers.YouTubeSearch;

import java.net.UnknownHostException;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class Main {

    public static void main(String[] args) throws UnknownHostException, MovieDbException, RottenTomatoesException {

        new Thread(new IMDBSearcher()).start();
        new Thread(new TMDBSearch()).start();
        new Thread(new YouTubeSearch()).start();
        new Thread(new RottenTomatoesImpl()).start();
    }
}