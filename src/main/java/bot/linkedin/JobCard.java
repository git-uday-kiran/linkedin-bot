package bot.linkedin;

import bot.enums.WorkType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

import static bot.linkedin.Locations.JOB_DESCRIPTION;
import static bot.utils.ThroatUtils.throatLow;

@Log4j2
@Getter
@Setter
public class JobCard {

	private String url;
	private String title;
	private String description;
	private String location;
	private String company;
	private boolean applied;
	private boolean viewed;

	private final BasePage basePage;
	private WebElement jobElement;

	private final char lineSeparator = '#';

	public JobCard(BasePage basePage) {
		this.basePage = basePage;
	}

	public JobCard(WebElement jobElement, BasePage basePage) {
		this.basePage = basePage;
		this.jobElement = jobElement;

		String jobText = jobElement.getText().toLowerCase();
		String[] lines = jobText.split("\\n");
		this.title = lines[0];
		this.company = lines[2];
		this.location = lines[3];

		applied = jobText.contains("applied");
		viewed = jobText.contains("viewed");
	}

	public WorkType getWorkType() {
		String workType = location.substring(location.indexOf('(') + 1, location.indexOf(')'));
		return switch (workType) {
			case "on-site" -> WorkType.ONSITE;
			case "remote" -> WorkType.REMOTE;
			case "hybrid" -> WorkType.HYBRID;
			default -> throw new IllegalStateException("Unexpected value: " + workType);
		};
	}

	public String getDescription() {
		if (description == null) click();
		return description;
	}

	public void click() {
		basePage.tryClick(jobElement);
		if (description == null) {
			throatLow();
			findAndStoreDescription();
			this.url = basePage.driver.getCurrentUrl();
		}
	}

	private void findAndStoreDescription() {
		description = basePage.waitForElementPresence(JOB_DESCRIPTION)
				.getText()
				.replace('\n', lineSeparator)
				.toLowerCase();
	}

	@Override
	public String toString() {
		return "JobCard{" +
				"title='" + title + '\'' +
				", applied=" + applied +
				", viewed=" + viewed +
				", location='" + location + '\'' +
				", company='" + company + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
