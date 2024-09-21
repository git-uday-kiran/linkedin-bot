package bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ConfigurationPropertiesScan
@PropertySource("classpath:job-filter.properties")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
