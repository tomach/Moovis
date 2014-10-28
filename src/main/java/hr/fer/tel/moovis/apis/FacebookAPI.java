package hr.fer.tel.moovis.apis;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

/**
 * Created by tomislaf on 28.10.2014..
 */
public class FacebookAPI {

    private static final String API_KEY = "538301972979920";
    private static final String APP_SECRET = "8a17d41e0334cda6f52af4b6481f92fa";
    private static final String CLIENT_TOKEN = "CAAHplTHhoNABAEvZAeWfvNK5FdFOIvXpxdPiMy3EmHAtWeNqPHjmH3YR6ibL1UZC8jsHvGUJVAr5tP5XnA1lN7ZAopKyCta1soZC2ZCNnNDjEOAY6f2f73RAKE9djv9N7v8MwDwdZAeTKsoH8w3VTYZCYEVsrCahPavt4QvY5lsswY0ZAnRopCyE5YIEZCIipecFdArv2eN17hm10SVwHGIpy";
    private Facebook facebook;

    public FacebookAPI()
    {
        facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthAccessToken(new AccessToken(CLIENT_TOKEN));
    }

    public Facebook getFacebook()
    {
        return facebook;
    }
}
