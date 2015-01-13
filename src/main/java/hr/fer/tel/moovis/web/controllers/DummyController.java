package hr.fer.tel.moovis.web.controllers;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DummyController {
	@Autowired
	private ApplicationUserRepository repo;

	@Autowired
	private RegistrationService regService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void facebookLogin() {

		regService
				.registerApplicationUser("CAAHplTHhoNABAG3CpyZAg9lic8uFfNtxbWzw7ww8OQrHGslCuF0UpiQ7wjrcHuV8U8ymI4JbhiBihdV5AmBWd9JKGZARAehnwLDVdFaTM7nHSRxOrUPihATLeDFWqSq09f5s78IZBSzAbFhAUgDmmA0VMvIZAZCiAXL3UZCfZAtizZCVm1ZCw34thFNW8y4ZAN89OfQ1lcYfNUlAtav5oxZAJPm");

	}
}
