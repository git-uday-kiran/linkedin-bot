package bot.linkedin.filters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "job.apply.filter")
public class JobsApplyFilter {

	private JobTitleFilter jobTitle;
	private JobDescFilter jobDesc;

	/**
	 * Applies jobs by clicking jobs, performing search query, applying advanced filters and starts applying jobs
	 */
	private boolean scanJobsInHomePage = false;

	/**
	 * Applies jobs by clicking jobs, performing search query, starts applying without applying advanced filters
	 */
	private boolean applyWithoutSearchFilter = false;

	@Data
	public static class JobDescFilter {
		private Set<String> excludeWords;
		private Set<String> includeWords;
	}

	@Data
	public static class JobTitleFilter {
		private Set<String> includeWords;
		private Set<String> excludeWords;
	}

}
