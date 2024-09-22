package bot.linkedin.services;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "job.apply.filter")
public class JobsApplyFilter {

	private JobTitleFilter jobTitle;
	private JobDescFilter jobDesc;
	private boolean scanJobsInHomePage = false;

	@Data
	static class JobDescFilter {
		private Set<String> excludeWords;
		private Set<String> includeWords;
	}

	@Data
	static class JobTitleFilter {
		private Set<String> includeWords;
	}

}
