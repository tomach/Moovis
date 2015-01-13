package hr.fer.tel.moovis.graphdb;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {

	IS_GENRE, SIMILAR, IS_DIRECTOR, CAST_IN
}
