package bot;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.ProfilesIni;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class Beans {

	@Bean
	WebDriver webDriver() {
		var profile = (new ProfilesIni()).getProfile("Default User");
		var options = new FirefoxOptions()
				.setProfile(profile);
		log.info("Loading driver...");
		var firefoxDriver = new FirefoxDriver(options);
		log.info("Driver loaded");
		return firefoxDriver;
	}

}
