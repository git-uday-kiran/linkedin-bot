package bot.enums;

import org.openqa.selenium.By;

public enum SortBy {

    MOST_RECENT(By.xpath("//span[text()='Most recent']")),
    MOST_RELEVANT(By.xpath("//span[text()='Most relevant']"));

    final By location;

    SortBy(By location) {
        this.location = location;
    }

    public By getLocation() {
        return this.location;
    }

}
