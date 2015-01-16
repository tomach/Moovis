package hr.fer.tel.moovis.graphdb;

import java.net.UnknownHostException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import com.mongodb.BasicDBList;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Neo4JImport {
	private static final String DB_PATH = "/home/filippm/neo4j-community-2.1.6/data/";

	private static final String DB_NAME = "moovis";

	private DB db;
	private DBCollection movies;
	private GraphDatabaseService graphDb;

	public Neo4JImport() throws UnknownHostException {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		registerShutdownHook(graphDb);
		MongoClient mongo = new MongoClient("localhost", 27017);
		db = mongo.getDB(DB_NAME);

		movies = db.getCollection("movies");
	}

	public void importDataToNeo() {
		DBCursor moviesCursor = movies.find();
		moviesCursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

		int i = 0;
		while (moviesCursor.hasNext()) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			i++;
			DBObject movie = moviesCursor.next();
			// System.out.println(movie);
			try (Transaction tx = graphDb.beginTx()) {
				Node movieNode = importBasicMovieData(movie);
				importMovieGenre(movie, movieNode);
				importMovieDirectors(movie, movieNode);
				importMovieCasts(movie, movieNode);
				// importSimilarMovies(movie, movieNode);
				tx.success();
			}

		}

	}

	private void importSimilarMovies(DBObject movie, Node movieNode) {
		if (movie.containsField("tmdb")) {
			DBObject tmdb = (DBObject) movie.get("tmdb");

			if (tmdb.containsField("similarMovies")) {
				BasicDBList similarMovies = (BasicDBList) tmdb
						.get("similarMovies");

				for (Object object : similarMovies) {
					DBObject similarMovie = (DBObject) object;

					Node similarMovieNode = getNode(NodeLabels.MOVIE, "title",
							similarMovie.get("title").toString().trim());

					if (similarMovieNode == null) {

						similarMovieNode = graphDb.createNode(NodeLabels.MOVIE);
						similarMovieNode.setProperty("title",
								similarMovie.get("title").toString().trim());
						similarMovieNode.setProperty("tmdbId", similarMovie
								.get("id").toString().trim());
					}
					movieNode.createRelationshipTo(similarMovieNode,
							RelTypes.SIMILAR);

				}
			}
		}
	}

	private Node importBasicMovieData(DBObject movie) {
		String movieTitle = movie.get("name").toString().trim();
		Node movieNode = getNode(NodeLabels.MOVIE, "title", movieTitle.trim());
		if (movieNode != null) {
			return movieNode;
		}

		movieNode = graphDb.createNode(NodeLabels.MOVIE);
		// System.out.println("Kreirao čvor za film");
		movieNode.setProperty("title", movieTitle.trim());
		if (movie.containsField("tmdb")) {
			movieNode.setProperty("tmdbId", ((DBObject) (movie.get("tmdb")))
					.get("id").toString());
		}

		return movieNode;
	}

	private void importMovieGenre(DBObject movie, Node movieNode) {
		if (movie.containsField("tmdb")) {
			DBObject tmdb = (DBObject) movie.get("tmdb");

			if (tmdb.containsField("genres")) {
				BasicDBList genres = (BasicDBList) tmdb.get("genres");
				for (Object object : genres) {
					String genre = object.toString();
					Node genreNode = getNode(NodeLabels.GENRE, "genre", genre);
					if (genreNode == null) {
						genreNode = graphDb.createNode(NodeLabels.GENRE);
						genreNode.setProperty("genre", genre);

					}
					movieNode
							.createRelationshipTo(genreNode, RelTypes.IS_GENRE);

				}
			}
		}
		if (movie.containsField("imdb")) {
			DBObject imdb = (DBObject) movie.get("imdb");

			if (imdb.containsField("genres")) {
				BasicDBList genres = (BasicDBList) imdb.get("genres");
				for (Object object : genres) {
					String genre = object.toString();
					Node genreNode = getNode(NodeLabels.GENRE, "genre", genre);
					if (genreNode == null) {
						genreNode = graphDb.createNode(NodeLabels.GENRE);
						genreNode.setProperty("genre", genre);

					}
					movieNode
							.createRelationshipTo(genreNode, RelTypes.IS_GENRE);

				}
			}
		}

	}

	private void importMovieDirectors(DBObject movie, Node movieNode) {
		if (movie.containsField("imdb")) {
			DBObject imdb = (DBObject) movie.get("imdb");

			if (imdb.containsField("directors")) {
				BasicDBList directors = (BasicDBList) imdb.get("directors");
				for (Object object : directors) {
					DBObject director = (DBObject) object;
					String directorName = director.get("name").toString();
					Node directorNode = getNode(NodeLabels.PERSON, "name",
							directorName);
					if (directorNode == null) {
						directorNode = graphDb.createNode(NodeLabels.PERSON);
						directorNode.setProperty("name", directorName);

					}
					directorNode.createRelationshipTo(movieNode,
							RelTypes.IS_DIRECTOR);

				}
			}
		}
	}

	private void importMovieCasts(DBObject movie, Node movieNode) {
		if (movie.containsField("tmdb")) {
			DBObject tmdb = (DBObject) movie.get("tmdb");

			if (tmdb.containsField("cast")) {
				BasicDBList actors = (BasicDBList) tmdb.get("cast");
				for (Object object : actors) {
					DBObject actor = (DBObject) object;
					String actorName = actor.get("name").toString();
					Node actorNode = getNode(NodeLabels.PERSON, "name",
							actorName);
					if (actorNode == null) {
						actorNode = graphDb.createNode(NodeLabels.PERSON);
						actorNode.setProperty("name", actorName);

					}
					actorNode.createRelationshipTo(movieNode, RelTypes.CAST_IN);
				}
			}
		}
	}

	private Node getNode(Label label, String propertyName, String propertyValue) {
		for (Node node : GlobalGraphOperations.at(graphDb)
				.getAllNodesWithLabel(label)) {
			if (node.hasProperty(propertyName)
					&& node.getProperty(propertyName).equals(propertyValue)) {
				return node;
			}

		}
		return null;

	}

	// START SNIPPET: shutdownHook
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public static void main(String[] args) throws UnknownHostException {
		new Neo4JImport().importDataToNeo();
	}
	// END SNIPPET: shutdownHook
}
