package hr.fer.tel.moovis.apis;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * Created by Damjan on 28.10.2014..
 */
public class TwitterAPI {

    //TODO ADD logic for user login
    private static final String CONSUMER_KEY = "YmwJeouoV8xDr0SuwImEoJ3M6";
    private static final String CONSUMER_SECRET = "9xRGoClKuJV53DOC6koUWjMzZezFxUgmqBEBLfoVlHdkmUPq57";

    private static final String ACCESS_TOKEN = "431485581-HkrCxQ1k1icRCYZSTCH7ws5ahAR3LcvFl8H7GBdX";
    private static final String ACCESS_TOKEN_SECRET = "MYBHALvABSvzD799EZT2uHnOJAnjtUZleAz81eTCpZGRm";

    private Twitter twitter;


    public TwitterAPI() {

        twitter = new TwitterFactory(getTwitterConfigurationWithToken().build()).getInstance();
    }


    public TwitterAPI(String token, String tokenSecret) {

        AccessToken accessToken = new AccessToken(token, tokenSecret);
        twitter = new TwitterFactory(getTwitterConfigurationNoToken().build()).getInstance(accessToken);
    }


    private ConfigurationBuilder getTwitterConfigurationWithToken() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        return cb;
    }


    private ConfigurationBuilder getTwitterConfigurationNoToken() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET);
        return cb;
    }


    public void getLatestTweets() {

        List<Status> statuses = null;

        try {
            statuses = twitter.getHomeTimeline();

            System.out.println("Showing twitter timeline:");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                        status.getText());
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }


    }


    public void searchForTweets(String queryString) {

        QueryResult result = null;

        try {
            Query query = new Query(queryString);
            result = twitter.search(query);

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }
    }
}
