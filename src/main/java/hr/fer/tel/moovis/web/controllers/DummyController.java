package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.model.ApplicationUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DummyController {
	@Autowired
	private ApplicationUserRepository repo;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void facebookLogin() {

		ApplicationUser user1 = repo.findOne(1L);
		ApplicationUser user2 = repo.findOne(2L);
		ApplicationUser user3 = repo.findOne(3L);

		user1.getFriends().add(user2);
		user2.getFriends().add(user1);

		user3.getFriends().add(user1);

		repo.save(user1);
		repo.save(user2);
		repo.save(user3);

	}
}
