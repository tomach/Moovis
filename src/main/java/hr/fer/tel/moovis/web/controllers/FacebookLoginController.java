package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonObject;


@Controller
public class FacebookLoginController {

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/login", method = RequestMethod.PUT)
	public ResponseEntity<JsonObject> facebookLogin(
			@RequestParam(value = "access_token") String accessToken) {

		ApplicationUser user = regService.registerApplicationUser(accessToken);
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("access_token", user.getAccessToken());
		
		return new ResponseEntity<JsonObject>(jsonObj, HttpStatus.OK);
	}
}