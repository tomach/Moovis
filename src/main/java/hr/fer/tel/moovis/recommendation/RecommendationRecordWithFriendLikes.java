package hr.fer.tel.moovis.recommendation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import hr.fer.tel.moovis.model.movie.Movie;

public class RecommendationRecordWithFriendLikes extends RecommendationRecord {

	private Set<String> friendNames;
	
	public RecommendationRecordWithFriendLikes(Movie movie, double recScore) {
		super(movie, recScore);
		this.friendNames = new HashSet<String>();
	}

	public Set<String> getFriendNames() {
		return Collections.unmodifiableSet(friendNames);
	}
	
	public void addFriendName(String name) {
		if (name != null) {
			friendNames.add(name);
		}
	}
}