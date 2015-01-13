package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.model.ApplicationUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FacebookLoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ApplicationUser facebookLogin(@RequestParam(value = "access_token") String accessToken) {
		
		
	
		return null;
	}
}
