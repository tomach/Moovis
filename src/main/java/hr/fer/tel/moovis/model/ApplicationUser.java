package hr.fer.tel.moovis.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "application_users")
public class ApplicationUser {
	@Id
	@GeneratedValue
	private Long id;
	private String accessToken;
	@Column(unique = true)
	private String facebookId;
	@Column(length = 1000)
	private String facebookAccessToken;
	private String name;
	private String surname;

	@ElementCollection
	@CollectionTable(name = "liked_movie_names")
	private Set<String> likedMovieNames = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "watched_movie_names")
	private Set<String> watchedMovieNames = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "watchlist_movie_names")
	private Set<String> watchList = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<ApplicationUser> friends = new HashSet<>();

	public ApplicationUser(String accessToken, String facebookId,
			String facebookAccessToken, String name, String surname,
			Set<String> likedMovieNames, Set<String> watchedMovieNames,
			Set<String> watchList, Set<ApplicationUser> friends) {
		super();
		this.accessToken = accessToken;
		this.facebookId = facebookId;
		this.facebookAccessToken = facebookAccessToken;
		this.name = name;
		this.surname = surname;
		this.likedMovieNames = likedMovieNames;
		this.watchedMovieNames = watchedMovieNames;
		this.friends = friends;
		this.watchList = watchList;
	}

	public ApplicationUser() {
	}

	public void addFriend(ApplicationUser friend) {
		if (friend == null) {
			throw new IllegalArgumentException("frined can not be null!");
		}
		friends.add(friend);
	}

	public void addLikedMovie(String movieName) {
		if (movieName == null) {
			throw new IllegalArgumentException("Movie name cannot be null!");
		}
		likedMovieNames.add(movieName);
	}

	public void resetLikedMovies() {
		likedMovieNames = new HashSet<String>();
	}

	public void addWatchedMovie(String movieName) {
		if (movieName == null) {
			throw new IllegalArgumentException("Movie name cannot be null!");
		}
		watchedMovieNames.add(movieName);
	}

	public void removeWatchedMovie(String movieName) {
		if (movieName == null) {
			throw new IllegalArgumentException("Movie name cannot be null!");
		}
		watchedMovieNames.remove(movieName);
	}

	public void addMovieToWatchList(String movieName) {
		if (movieName == null) {
			throw new IllegalArgumentException("Movie name cannot be null!");
		}
		watchList.add(movieName);
	}

	public void removeMovieToWatchList(String movieName) {
		if (movieName == null) {
			throw new IllegalArgumentException("Movie name cannot be null!");
		}
		watchList.remove(movieName);
	}

	public Set<String> getWatchList() {
		return Collections.unmodifiableSet(watchList);
	}

	public Set<String> getWatchedMovieNames() {
		return Collections.unmodifiableSet(watchedMovieNames);
	}

	public Set<String> getLikedMovieNames() {
		return Collections.unmodifiableSet(likedMovieNames);
	}

	public Set<ApplicationUser> getFriends() {
		return Collections.unmodifiableSet(friends);
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

	public Long getId() {
		return id;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationUser other = (ApplicationUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApplicationUser [id=" + id + ", accessToken=" + accessToken
				+ ", facebookId=" + facebookId + ", facebookAccessToken="
				+ facebookAccessToken + ", name=" + name + ", surname="
				+ surname + ", likedMovieNames=" + likedMovieNames
				+ ", friends=" + friends.size() + "]";
	}

}