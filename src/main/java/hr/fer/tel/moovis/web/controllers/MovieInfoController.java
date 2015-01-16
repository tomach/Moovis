package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.names.MovieNamesContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MovieInfoController {

	@Autowired
	private MovieDao movieDao;
	
	
	@RequestMapping(value = "/movie/{name}", method = RequestMethod.GET)
	public ResponseEntity<Movie> getMovie(
			@PathVariable(value = "name") String movieName) {

		Movie mov = movieDao.findMovieByName(MovieNamesContainer.getInstance()
				.getMovieName(movieName));
		System.out.println(mov);
		return new ResponseEntity<Movie>(mov, HttpStatus.OK);
	}
}