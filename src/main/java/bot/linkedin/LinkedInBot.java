package bot.linkedin;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static bot.utils.Utils.sleep;

@Log4j2
@Component
public class LinkedInBot extends BasePage implements ApplicationRunner {

	private final JobFilter filter;
	private final EasyJobApplier applier;

	public LinkedInBot(WebDriver driver, JobFilter filter, EasyJobApplier applier) {
		super(driver);
		this.filter = filter;
		this.applier = applier;
	}

	@Override
	public void run(ApplicationArguments args) {
//		filter.setSearchQuery("java");
//		applier.init();
//		applier.apply(filter);
		sleep(100000);
	}

}
