package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.dao.MovieDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.recommendation.MovieRecommendation;
import hr.fer.tel.moovis.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DummyController {

	@Autowired
	private MovieDao movieDao;
	@Autowired
	private MovieRecommendation movieRec;
	@Autowired
	private ApplicationUserRepository repo;

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void facebookLogin() {

		regService
				.registerApplicationUser("CAAHplTHhoNABANZBELFa4DVoboOZAO4FyXFvYk1r5oIACgoCJqHgumB5tUEeYd5BRDYqtMApBtAZAmuPukaNSiPjifxNaJZC9l602nX6KU0YOR1B9hZAmPdbGw1djwMCQfutXf3skjpWRD5qg5LSlPZBG4oBxCPZCIUupJObaYDjmQIRQf2tfLoZCWYhL6fVbpqWVfNCfZBURMdLlUsmDsrU2OS4lNs6V6dMZD");

	}

	@RequestMapping(value = "/movie", method = RequestMethod.GET)
	public void env() {

		System.out.println(movieDao.findMovieByName("Interstellar"));
		System.out.println(movieDao.findMovieByName("Interstellar")
				.getSimilarMovies());

	}

	@RequestMapping(value = "/rec", method = RequestMethod.GET)
	public void env1() {
		movieRec.calculateRecommendation(repo
				.findByFacebookId("1375227446117765"));

	}

}
