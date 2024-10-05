package bot.linkedin.filters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "job.apply.filter")
public class JobsApplyFilter {

	private JobTitleFilter jobTitle;
	private JobDescFilter jobDesc;


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
