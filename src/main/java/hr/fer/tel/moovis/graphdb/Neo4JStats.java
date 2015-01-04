package hr.fer.tel.moovis.graphdb;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

public class Neo4JStats {
	private static final String DB_PATH = "/home/filippm/neo4j-community-2.1.6/data/graph.db";

	private GraphDatabaseService graphDb;

	public Neo4JStats() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
	}

	public void delete() {
		try (Transaction tx = graphDb.beginTx()) {

			int rels = 0;
			for (Relationship rel : GlobalGraphOperations.at(graphDb)
					.getAllRelationships()) {
				rel.delete();
				rels++;
			}
			int nodes = 0;
			for (Node node : GlobalGraphOperations.at(graphDb).getAllNodes()) {
				node.delete();
				nodes++;
			}

			System.out.println("Deleted nodes:" + nodes);
			System.out.println("Deleted rels:" + rels);
			tx.success();
		}

	}

	public void stats() {
		try (Transaction tx = graphDb.beginTx()) {

			int rels = 0;
			for (Relationship rel : GlobalGraphOperations.at(graphDb)
					.getAllRelationships()) {
				rels++;
			}
			int nodes = 0;
			for (Node node : GlobalGraphOperations.at(graphDb).getAllNodes()) {
				nodes++;
			}

			System.out.println(" nodes:" + nodes);
			System.out.println(" rels:" + rels);
			tx.success();
		}

	}

	public static void main(String[] args) {
		Neo4JStats obj = new Neo4JStats();
		obj.stats();
		//obj.delete();
		obj.graphDb.shutdown();
		// new Neo4JStats().delete();
	}
}
