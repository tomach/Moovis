package hr.fer.tel.moovis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by filippm on 10.11.14..
 */
@EnableAutoConfiguration
@ComponentScan
public class StarterApp {

	public static void main(String[] args) {
		SpringApplication.run(StarterApp.class, args);
	}
}