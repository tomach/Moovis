package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.service.RegistrationService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FacebookLoginController {

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/facebook_login", method = RequestMethod.PUT)
	public ResponseEntity<String> facebookLogin(
			@RequestParam(value = "facebook_access_token") String facebookAccessToken) {

		System.out.println("Login start" + this);
		System.out.println("Login start current thread:"
				+ Thread.currentThread().getId());

		ApplicationUser user;
		try {
			user = regService.registerApplicationUser(facebookAccessToken);
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("error", e.getLocalizedMessage());
			return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("access_token", user.getAccessToken());

		System.out.println("Login end" + this);
		System.out.println("Login end current thread:"
				+ Thread.currentThread().getId());
		return new ResponseEntity<String>(jsonObj.toString(), HttpStatus.OK);
	}
}