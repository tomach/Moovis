package hr.fer.tel.moovis;

import hr.fer.tel.moovis.dao.ApplicationUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by filippm on 10.11.14..
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
public class StarterApp extends SpringBootServletInitializer {
	@Autowired
	ApplicationUserRepository rep;

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(StarterApp.class);
	}

	public static void main(String[] args) {
		System.out.println("Starting...");
		SpringApplication.run(StarterApp.class, args);
		
		System.out.println(System.getenv());
		System.out.println(System.getProperties().keySet());
		System.out.println("Started");
	}
}