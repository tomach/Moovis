package hr.fer.tel.moovis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by filippm on 10.11.14..
 */
@EnableAutoConfiguration
@ComponentScan
public class StarterApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(StarterApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StarterApp.class, args);
	}
}