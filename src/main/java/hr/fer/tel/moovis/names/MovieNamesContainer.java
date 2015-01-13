package hr.fer.tel.moovis.names;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

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

	private Set<String> movieNames;

	private MovieNamesContainer() throws UnknownHostException {
		System.out.println("Loading movie names...");
		movieNames = new HashSet<>();
		// Since 2.10.0, uses MongoClient
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("moovis");

		DBCollection movies = db.getCollection("movies");

		Cursor cur = movies.find();
		while (cur.hasNext()) {
			movieNames.add(cur.next().get("name").toString());
		}
		System.out.println("Loading over!");
	}

	public String getMovieName(String nameCandidate) {
		if (movieNames.contains(nameCandidate)) {
			return nameCandidate;
		} else {
			return getMovieNameWithSimilarity(nameCandidate);
		}
	}

	private String getMovieNameWithSimilarity(String nameCandidate) {
		double similarity = 0.0;
		JaroWinkler algorithm = new JaroWinkler();
		String retVal = null;
		for (String name : movieNames) {
			double tempSim = algorithm.getSimilarity(nameCandidate, name);
			if (tempSim > similarity) {
				retVal = name;
				similarity = tempSim;
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
