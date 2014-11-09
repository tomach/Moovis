package hr.fer.tel.moovis.apis;

import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbMovie;
import com.moviejukebox.imdbapi.model.ImdbMovieDetails;
import com.moviejukebox.imdbapi.model.ImdbSearchResult;
import com.moviejukebox.imdbapi.search.SearchObject;
import facebook4j.*;
import facebook4j.Movie;
import facebook4j.auth.AccessToken;
import hr.fer.tel.moovis.model.*;
import hr.fer.tel.moovis.model.User;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class FacebookAPI {

    private static final String API_KEY = "538301972979920";
    private static final String APP_SECRET = "8a17d41e0334cda6f52af4b6481f92fa";
    private static final String CLIENT_TOKEN = "CAAHplTHhoNABAEvZAeWfvNK5FdFOIvXpxdPiMy3EmHAtWeNqPHjmH3YR6ibL1UZC8jsHvGUJVAr5tP5XnA1lN7ZAopKyCta1soZC2ZCNnNDjEOAY6f2f73RAKE9djv9N7v8MwDwdZAeTKsoH8w3VTYZCYEVsrCahPavt4QvY5lsswY0ZAnRopCyE5YIEZCIipecFdArv2eN17hm10SVwHGIpy";
    private static final String ACC_TOKEN = "CAAHplTHhoNABAKh4npatMU3Dp9oyDwIqzV3KImc4Bfwnmi8BCKd716WgGg1xVfGsQqbg14nZCfSYOcG73ZAfmMKjVZAQgI7PBZCXyPvvQfZCIBergQepnKxnQUAqYwyWbTMgO9i4eDbW6GCyJPhEagv8sbLHFzRfIKqobeGj4dPooxArDoGJREtAipWNuZAUlTkksr82WzDQTHrq0v33p3\n";
    private Facebook facebook;

    public FacebookAPI()
    {
        facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthPermissions("user_likes");
        facebook.setOAuthAccessToken(new AccessToken(ACC_TOKEN, null));
    }

    public Facebook getFacebook()
    {
        return facebook;
    }


    public static void main(String[] args) throws FacebookException{

        Facebook f = new FacebookAPI().getFacebook();
        User u = new User(f.getName(),"");
        ResponseList<Movie> likedMovies = f.getMovies();
        System.out.println(likedMovies);
        List<hr.fer.tel.moovis.model.Movie> listMovies = new ArrayList<hr.fer.tel.moovis.model.Movie>();
        for (Movie fbM : likedMovies){

            hr.fer.tel.moovis.model.Movie movie = new hr.fer.tel.moovis.model.Movie(fbM.getName(), "");
            Group g = f.getGroup(fbM.getId());
            movie.setDescription(g.getDescription());
            listMovies.add(movie);
        }
        System.out.println(listMovies);
       u.setLikedMovies(listMovies);


        //System.out.println(ImdbApi.getSearch("gone girl"));
        //System.out.println(ImdbApi.getUserReviews("tt2267998"));
        System.out.println(ImdbApi.getFullDetails("tt2267998").getRating());
        System.out.println(ImdbApi.getFullDetails("tt2267998").getTrailer());


    }
}
