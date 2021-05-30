package nl.rso.axon.happergame;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@EnableReactiveMongoRepositories
public class HappergameApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(HappergameApplication.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}
}
