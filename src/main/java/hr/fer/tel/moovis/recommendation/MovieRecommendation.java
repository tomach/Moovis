package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.model.ApplicationUser;

import java.util.List;


public interface MovieRecommendation {

	public List<RecommendationRecord> calculateRecommendation(ApplicationUser user);

}
