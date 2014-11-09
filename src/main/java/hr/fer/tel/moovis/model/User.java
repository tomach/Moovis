package hr.fer.tel.moovis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomislaf on 9.11.2014..
 */
public class User {

    private long userId;
    private String name;
    private String surname;
    private String facebookAccessToken;
    private String twitterAccessToken;
    private List<Movie> likedMovies;
    private List<Movie> friendsLikedMovies;

    public User(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.likedMovies = new ArrayList<Movie>();
        this.friendsLikedMovies = new ArrayList<Movie>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    public String getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public void setTwitterAccessToken(String twitterAccessToken) {
        this.twitterAccessToken = twitterAccessToken;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public void addLikedMovies(List<Movie> likedMovies){
        this.likedMovies.addAll(likedMovies);
    }

    public List<Movie> getFriendsLikedMovies() {
        return friendsLikedMovies;
    }

    public void setFriendsLikedMovies(List<Movie> friendsLikedMovies) {
        this.friendsLikedMovies = friendsLikedMovies;
    }

    public void addFriendsLikedMovies(List<Movie> friendsLikedMovies) {
        this.friendsLikedMovies.addAll(friendsLikedMovies);
    }
}
