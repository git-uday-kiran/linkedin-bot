package bot.linkedin.filters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("job.apply.config")
public class JobApplyConfig {

	private boolean scanJobsInHomePage = false;

	private boolean applyWithoutSearchFilter = false;

	private boolean skipViewedJobs = false;

	private int allowMaxYOE = 3;

	private List<String> jobsUrls = new ArrayList<>();

	private List<String> easyApplyJobUrls = new ArrayList<>();

}
