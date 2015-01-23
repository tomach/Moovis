package hr.fer.tel.moovis;

import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;

import hr.fer.tel.moovis.names.MovieNamesContainer;
import hr.fer.tel.moovis.searchers.rotten.RottenCollector;
import hr.fer.tel.moovis.service.FacebookLikesUpdaterProcess;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.omertron.rottentomatoesapi.RottenTomatoesException;

/**
 * Created by filippm on 10.11.14..
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = "hr.fer.tel")
@Configuration
public class StarterApp extends SpringBootServletInitializer {

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(
				Charset.forName("UTF-8"));
		converter.setWriteAcceptCharset(false);
		return converter;
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		Hibernate4Module hm = new Hibernate4Module();
		hm.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING, false);

		mapper.registerModule(hm);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		messageConverter.setObjectMapper(mapper);
		return messageConverter;
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		System.out.println("Starting...");
		MovieNamesContainer.getInstance();
		try {
			RottenCollector.main(null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (RottenTomatoesException e) {
			e.printStackTrace();
		}
		return application.sources(applicationClass);
	}

	private static Class<StarterApp> applicationClass = StarterApp.class;

	public static void main(String[] args) {
		System.out.println("Starting...");
		MovieNamesContainer.getInstance();
		try {
			RottenCollector.main(null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (RottenTomatoesException e) {
			e.printStackTrace();
		}

		SpringApplication.run(StarterApp.class, args);

		System.out.println(System.getenv());
		System.out.println(System.getProperties().keySet());
		System.out.println("Started");
	}
}