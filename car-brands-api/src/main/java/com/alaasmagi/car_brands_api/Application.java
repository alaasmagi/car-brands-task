package com.alaasmagi.car_brands_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		loadDotenv();
		SpringApplication.run(Application.class, args);
	}

	private static void loadDotenv() {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry -> {
			String key = entry.getKey();
			if (System.getenv(key) == null && System.getProperty(key) == null) {
				System.setProperty(key, entry.getValue());
			}
		});
	}

}
