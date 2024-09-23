package bot;

import bot.linkedin.services.JobsDayCount;
import bot.linkedin.services.JobsDayCountRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@ConfigurationPropertiesScan
@PropertySource("classpath:filter.properties")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
