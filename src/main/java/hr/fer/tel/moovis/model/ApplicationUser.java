package hr.fer.tel.moovis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import facebook4j.Friend;


public class ApplicationUser {

	private String accessToken;
	private String facebookAccessToken;
	private String name;
	private String surname;
	private List<ApplicationUser> friendsList;
	private List<String> movieList;
	private Date lastUpdate; 
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public ApplicationUser(String accessToken, String facebookAccessToken, String name, String surname) {
		this.accessToken = accessToken;
		this.facebookAccessToken = facebookAccessToken;
		this.name = name;
		this.surname = surname;
		
		this.friendsList = new ArrayList<ApplicationUser>();
		this.movieList = new ArrayList<String>();
	}
	
	public void addFriend(Friend friend) {
		String friendID = friend.getId();
		
	}
	
	public List<ApplicationUser> getFriendsList() {
		return friendsList;
	}

	public List<String> getMovieList() {
		return movieList;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getFacebookAccessToken() {
		return facebookAccessToken;
	}
	
	public void setFacebookAccessToken(String facebookAccessToken) {
		this.facebookAccessToken = facebookAccessToken;
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
}