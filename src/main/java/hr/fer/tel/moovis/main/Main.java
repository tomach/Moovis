package hr.fer.tel.moovis.main;

import com.moviejukebox.imdbapi.ImdbApi;
import com.moviejukebox.imdbapi.model.ImdbReview;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import hr.fer.tel.moovis.apis.FacebookAPI;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class Main {

    public static void main(String[] args) {
        Facebook f = new FacebookAPI().getFacebook();
        try {
            f.getFriends();
        } catch (FacebookException e) {
            e.printStackTrace();
        }

        //System.out.println(ImdbApi.getSearch("gone girl"));
        //System.out.println(ImdbApi.getUserReviews("tt2267998"));
        System.out.println(ImdbApi.getFullDetails("tt2267998").getRating());
        System.out.println(ImdbApi.getFullDetails("tt2267998").getTrailer());


    }
}