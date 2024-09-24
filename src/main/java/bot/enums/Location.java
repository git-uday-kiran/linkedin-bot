package bot.enums;

import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public enum Location {

	BENGALURU(By.xpath("//span[text()='Bengaluru']")),
	HYDERABAD(By.xpath("//span[text()='Hyderabad']")),
	PUNE(By.xpath("//span[text()='Pune']")),
	CHENNAI(By.xpath("//span[text()='Chennai']")),
	BENGALURU_EAST(By.xpath("//span[text()='Bengaluru East']")),
	NOIDA(By.xpath("//span[text()='Noida']")),
	GURUGRAM(By.xpath("//span[text()='Gurugram']")),
	GURGAON(By.xpath("//span[text()='Gurgaon']")),
	MUMBAI(By.xpath("//span[text()='Mumbai']")),
	AHMEDABAD(By.xpath("//span[text()='Ahmedabad']")),
	TRIVANDRUM(By.xpath("//span[text()='Trivandrum']"));

	private final By location;

	Location(By location) {
		this.location = location;
	}
}
