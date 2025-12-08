package bot;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class Beans {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Beans.class);

    @Bean
    WebDriver webDriver() {
        // Firefox installed as snap - profile and binary paths are different
        String profilePath = System.getProperty("user.home") +
                "/snap/firefox/common/.mozilla/firefox/igs2ldat.uday-kiran-personal";
        String firefoxBinary = "/snap/firefox/current/usr/lib/firefox/firefox";

        FirefoxProfile profile = new FirefoxProfile(new File(profilePath));
        log.info("Loading Firefox profile from: {}", profilePath);

        var options = new FirefoxOptions()
                .setProfile(profile)
                .setBinary(firefoxBinary);
        log.info("Using Firefox binary: {}", firefoxBinary);
        log.info("Loading driver...");
        var firefoxDriver = new FirefoxDriver(options);
        log.info("Driver loaded");
        return firefoxDriver;
    }

}
