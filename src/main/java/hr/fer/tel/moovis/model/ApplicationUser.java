package hr.fer.tel.moovis.model;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

=======
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
>>>>>>> 8ab08fc5a1e38585bee2ab0831ac6ed96e432532

@Entity
@Table(name = "application_users")
public class ApplicationUser {
	@Id
	@GeneratedValue
	private Long id;
	private String accessToken;

	private String facebookId;
	private String facebookAccessToken;
	private String name;
	private String surname;
<<<<<<< HEAD
	private String email;
	private String password;
	private List<ApplicationUser> friendsList;
	private List<String> movieList;
	private Date lastMovieUpdate; 
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getLastMovieUpdate() {
		return lastMovieUpdate;
	}
=======
>>>>>>> 8ab08fc5a1e38585bee2ab0831ac6ed96e432532

	@ElementCollection
	@CollectionTable(name = "movie_names")
	private Set<String> likedMovieNames = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<ApplicationUser> friends = new HashSet<>();

	public ApplicationUser(String accessToken, String facebookId,
			String facebookAccessToken, String name, String surname,
			Set<String> likedMovieNames, Set<ApplicationUser> friends) {
		super();
		this.accessToken = accessToken;
		this.facebookId = facebookId;
		this.facebookAccessToken = facebookAccessToken;
		this.name = name;
		this.surname = surname;
		this.likedMovieNames = likedMovieNames;
		this.friends = friends;
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
<<<<<<< HEAD
	
	
	public List<ApplicationUser> getFriendsList() {
		return friendsList;
=======

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

	public Set<String> getLikedMovieNames() {
		return Collections.unmodifiableSet(likedMovieNames);
>>>>>>> 8ab08fc5a1e38585bee2ab0831ac6ed96e432532
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

}