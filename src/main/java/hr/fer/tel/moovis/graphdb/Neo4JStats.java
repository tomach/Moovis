package hr.fer.tel.moovis.graphdb;

import java.util.Iterator;

import javax.management.RuntimeErrorException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

public class Neo4JStats {
	private static final String DB_PATH = "/home/filippm/neo4j-community-2.1.6/data/";

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
				if (rels % 1000 == 0) {
					System.out.println(rels);
				}
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

			long nodes = 0;
			for (Iterator<Node> iterator = GlobalGraphOperations.at(graphDb)
					.getAllNodes().iterator(); iterator.hasNext();) {
				if (nodes % 1000 == 0) {
					System.out.println(nodes);
				}
				nodes++;
			}
			if (nodes > 1) {
				throw new RuntimeException();
			}
			long rels = 0;
			for (Iterator<Relationship> iterator = GlobalGraphOperations
					.at(graphDb).getAllRelationships().iterator(); iterator
					.hasNext();) {
				if (rels % 1000 == 0) {
					System.out.println(rels);
				}
				rels++;
			}

			System.out.println(" nodes:" + nodes);
			System.out.println(" rels:" + rels);
			tx.success();
		}

	}

	public static void main(String[] args) {
		Neo4JStats obj = new Neo4JStats();
		obj.stats();
		// obj.delete();
		obj.graphDb.shutdown();
		// new Neo4JStats().delete();
	}
}
