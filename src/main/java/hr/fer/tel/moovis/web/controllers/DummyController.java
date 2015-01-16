package hr.fer.tel.moovis.web.controllers;

import java.util.List;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.exceptions.FacebookLoginException;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.model.movie.Movie;
import hr.fer.tel.moovis.recommendation.MovieRecommendation;
import hr.fer.tel.moovis.recommendation.MovieRecommendationImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationOnlyFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.MovieRecommendationWithFriendScoreImpl;
import hr.fer.tel.moovis.recommendation.RecommendationRecord;
import hr.fer.tel.moovis.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DummyController {
	
	@Autowired
	private MovieRecommendationOnlyFriendScoreImpl movieRecFriend;
	@Autowired
	private MovieDao movieDao;
	@Autowired
	private MovieRecommendationImpl movieRec;
	@Autowired
	private ApplicationUserRepository appUserRepo;
	@Autowired
	private MovieRecommendationWithFriendScoreImpl movieRecWithFriend;

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void facebookLogin() {

		try {
			regService
					.registerApplicationUser("CAAHplTHhoNABAFzZBQ8BXJMZCEJCtvPdpf6KY0SeiSlVoN8GITAtLwPRy1OUpOIVGcSZBINZBCpyfM2VurfZBw4SgjeG0NIEJ422wxvYfG14syYrjER6o2RVo4D4vGJqgigbEhSfAhbnstXOyV771jgpdyHGzfcqG9ebXmaUP4EnpDx35SaT32GpmBkiFu7Xte25FtAifeaJxAQg1y2WA");
		} catch (FacebookLoginException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/test_rec", method = RequestMethod.GET)
	public void testRec() {

		ApplicationUser user = appUserRepo.
				findByAccessToken("02eb2504-17f8-33c3-8d25-9325b0235201");
		movieRecFriend.calculateRecommendation(user);

	}
	
	@RequestMapping(value = "/test2_rec_all", method = RequestMethod.GET)
	public void testRecAll() {

		ApplicationUser user = appUserRepo.
				findByAccessToken("02eb2504-17f8-33c3-8d25-9325b0235201");
		movieRecWithFriend.calculateRecommendation(user);

	}

	@RequestMapping(value = "/movie", method = RequestMethod.GET)
	public ResponseEntity<Movie> getMovie() {

		Movie mov = movieDao.findMovieByName("Interstellar");
		System.out.println(mov);
		System.out.println(mov.getSimilarMovies());
		return new ResponseEntity<Movie>(mov, HttpStatus.OK);
	}

	@RequestMapping(value = "/rec", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> env1(
			@RequestParam(value = "access_token") String accessToken) {

		System.out.println(accessToken);
		ApplicationUser user = appUserRepo.findByAccessToken(accessToken);
		System.out.println(user);
		return new ResponseEntity<List<Movie>>(
				movieRec.calculateRecommendation(user), HttpStatus.OK);

	}

}
