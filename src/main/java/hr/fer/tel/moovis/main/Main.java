package hr.fer.tel.moovis.main;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import hr.fer.tel.moovis.apis.FacebookAPI;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class Main {

    public static void main(String[]args)
    {
        Facebook f = new FacebookAPI().getFacebook();
        try {
            f.getUserLikes();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
    }
}
