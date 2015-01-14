package hr.fer.tel.moovis.recommendation;

import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;

import java.util.List;

import org.springframework.stereotype.Service;

public interface MovieRecommendation {

	public List<Movie> calculateRecommendation(ApplicationUser user);

}
