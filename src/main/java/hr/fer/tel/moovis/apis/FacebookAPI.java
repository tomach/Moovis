package hr.fer.tel.moovis.apis;

import com.mongodb.*;
import facebook4j.*;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONException;
import hr.fer.tel.moovis.model.User;
import hr.fer.tel.moovis.searchers.IMDBSearcher;

import java.net.UnknownHostException;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class FacebookAPI {

    private static final String API_KEY = "538301972979920";
    private static final String APP_SECRET = "8a17d41e0334cda6f52af4b6481f92fa";
    private static final String CLIENT_TOKEN = "CAAHplTHhoNABAEvZAeWfvNK5FdFOIvXpxdPiMy3EmHAtWeNqPHjmH3YR6ibL1UZC8jsHvGUJVAr5tP5XnA1lN7ZAopKyCta1soZC2ZCNnNDjEOAY6f2f73RAKE9djv9N7v8MwDwdZAeTKsoH8w3VTYZCYEVsrCahPavt4QvY5lsswY0ZAnRopCyE5YIEZCIipecFdArv2eN17hm10SVwHGIpy";
    private static final String ACC_TOKEN = "CAAHplTHhoNABAMto00JvlNcjZAfyHpH45nIetnQnzXV2QYkpf5pNbvurV3XGaybfc9gq9kyjQDpOYOK9fUneSU8S5E1rLNGzGb0Ujv6y7MXjIo813ZCezURo2xAm10wdkYCikMXWqX7YhGHkY7kPCs8on7WzIilyX75M5qcH42DTBRKw2YB1JUOQs9zKk612XZBClunNLpranYHg97s\n";
    private Facebook facebook;


    public FacebookAPI() throws UnknownHostException {
        facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthPermissions("user_likes");
        facebook.setOAuthAccessToken(new AccessToken(ACC_TOKEN, null));

    }

    public Facebook getFacebook() {
        return facebook;
    }


    public static void main(String[] args) throws FacebookException, UnknownHostException, JSONException {

        MongoClient mongo;
        DB dbMoovis;
        DBCollection rottenSearchQueue;
        DBCollection tmdbSearchQueue;
        DBCollection ytbSearchQueue;

        DBCollection movies;

        mongo = new MongoClient("localhost", 27017);
        dbMoovis = mongo.getDB("moovis");
        rottenSearchQueue = dbMoovis.getCollection("RottenSearchQueue");
        tmdbSearchQueue = dbMoovis.getCollection("TMDBSearchQueue");
        ytbSearchQueue = dbMoovis.getCollection("YTSearchQueue");
        movies = dbMoovis.getCollection("movies");

        Facebook f = new FacebookAPI().getFacebook();
        User u = new User(f.getName(), "");
        ResponseList<Movie> likedMovies = f.getMovies();

        System.out.println("fb liked movies " + likedMovies);

        //List<hr.fer.tel.moovis.model.Movie> listMovies = new ArrayList<hr.fer.tel.moovis.model.Movie>();
        for (Movie fbM : likedMovies) {
            DBObject dbMovie = new BasicDBObject().append("movieKey", fbM.getName());
            Cursor checkExists = movies.find(dbMovie);
            if (!checkExists.hasNext()) {
                ytbSearchQueue.insert(dbMovie);
                rottenSearchQueue.insert(dbMovie);
                tmdbSearchQueue.insert(dbMovie);
                /*
                hr.fer.tel.moovis.model.Movie movie = new hr.fer.tel.moovis.model.Movie(fbM.getName(), "");

                Group g = f.getGroup(fbM.getId());
                Page p = f.getPage(fbM.getId());
                JSONObject obj = f.rawAPI().callGetAPI(fbM.getId()).asJSONObject();
                String dat = obj.getString("release_date");
                System.out.println("releaseDate " + dat);
                movie.setDescription(g.getDescription());
                listMovies.add(movie);
                */
            }
            checkExists.close();
        }
    }
}
