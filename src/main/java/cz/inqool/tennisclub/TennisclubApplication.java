package cz.inqool.tennisclub;

import cz.inqool.tennisclub.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class TennisclubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisclubApplication.class, args);
	}

}
