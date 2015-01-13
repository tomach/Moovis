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

		regService
				.registerApplicationUser("CAAHplTHhoNABAIVJQs1wfrXNNpskiFRmIlvQzFfbSesMYlbfBBBhSQsN8SZB1oavX7vDh5g2Ybf4Tb1rHv61KkSmnWhvcByPs1Luf3tYXHTuDRM6anxLanyZCIIZBbV58WZCcdWK1MpqLDhyjHX73lTQZB8UyqegpCr3FvlEoyDHGlerTRPqBiTLBkFWJmzIJymvvtj975BgIfVQWe9iy");

		regService
				.registerApplicationUser("CAAHplTHhoNABAKGIZCu4KQ5XlGAQY7UhlnjD9Mtjw9CusQ1UKXR0qalaZCKUZBPPYGZAegVdeUulGbgvHhroZBY3c6fZAJmSz8PDgXPEN3UCb4VCrnUKvw0fhWCOyduTMpuiTytqGuLHi3HSq0PNjZB6lwhI7e0KV6ZB2z7gJU3j3UBWQM53TazMJYdQ1BJLBOhEcEnaZBlQJL7nY8KtskYXR");

	}
}
