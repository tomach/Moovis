package hr.fer.tel.moovis.names;

import hr.fer.tel.moovis.MongoConnections;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MovieNamesContainer {

	private static MovieNamesContainer instance;
	static {
		try {
			instance = new MovieNamesContainer();
		} catch (UnknownHostException e) {
		}
	}
	// namelowerCase:nameNormal
	private Map<String, String> names;

	private Set<String> movieNames;
	private Set<String> movieNamesLowerCased;

	private MovieNamesContainer() throws UnknownHostException {
		System.out.println("Loading movie names...");
		movieNames = new HashSet<>();
		movieNamesLowerCased = new HashSet<>();
		names = new HashMap<>();
		DB db = MongoConnections.getInstance().getDb();

		DBCollection movies = db.getCollection("movies");

		Cursor cur = movies.find();
		while (cur.hasNext()) {
			String tempName = cur.next().get("movieKey").toString().trim();
			// movieNames.add(tempName);
			// movieNamesLowerCased.add(tempName.toLowerCase());
			if (!names.containsKey(tempName.toLowerCase())) {
				names.put(tempName.toLowerCase(), tempName);
			}
		}
		cur.close();
		System.out.println("Loading over!");
	}

	public String getMovieName(String nameCandidate) {
		String res;
		nameCandidate = nameCandidate.trim();
		if (names.containsKey(nameCandidate.toLowerCase())) {
			res = names.get(nameCandidate.toLowerCase());
		} else {
			res = getMovieNameWithSimilarity(nameCandidate);
		}
		System.out.println(nameCandidate + "\n" + res);
		return res;
	}

	private String getMovieNameWithSimilarity(String nameCandidate) {
		double similarity = 0.0;
		JaroWinkler algorithm = new JaroWinkler();
		String retVal = null;
		String nameCandidateToLower = nameCandidate.toLowerCase();

		if (nameCandidateToLower.startsWith("the")) {
			String nameCanWithoutThe = nameCandidateToLower.substring(4);
			double simWithoutThe = 0.0;
			double simWithThe = 0.0;
			String retValWitoutThe = null;
			String retValWithThe = null;
			for (String key : names.keySet()) {
				double tempSimWithThe = algorithm.getSimilarity(
						nameCandidateToLower, key);
				double tempSimWitoutThe = algorithm.getSimilarity(
						nameCanWithoutThe, key);

				if (tempSimWithThe > simWithThe) {
					retValWithThe = names.get(key);
					simWithThe = tempSimWithThe;
				}
				if (tempSimWitoutThe > simWithoutThe) {
					retValWitoutThe = names.get(key);
					simWithoutThe = tempSimWitoutThe;
				}

			}

			if (simWithoutThe > simWithThe) {
				return retValWitoutThe;
			} else {
				return retValWithThe;
			}
		} else {
			for (String key : names.keySet()) {
				double tempSim = algorithm.getSimilarity(nameCandidateToLower,
						key);
				if (tempSim > similarity
						&& key.length() <= nameCandidate.length()) {
					retVal = names.get(key);
					similarity = tempSim;
				}
			}
		}

		return retVal;
	}

	public static MovieNamesContainer getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		System.out.println(MovieNamesContainer.getInstance()
				.getMovieNameWithSimilarity("Pearl harbour movie"));
		System.out.println(MovieNamesContainer.getInstance()
				.getMovieNameWithSimilarity("Interstelar movie"));
	}

}
