package bot.linkedin.filters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("job.apply.config")
public class JobApplyConfig {

	/**
	 * Applies jobs by clicking jobs, performing search query, applying advanced filters and starts applying jobs
	 */
	private boolean scanJobsInHomePage = false;

	/**
	 * Applies jobs by clicking jobs, performing search query, starts applying without applying advanced filters
	 */
	private boolean applyWithoutSearchFilter = false;

	private List<String> manualJobsUrls = new ArrayList<>();

}
