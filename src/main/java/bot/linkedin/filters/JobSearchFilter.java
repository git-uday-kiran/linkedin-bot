package bot.linkedin.filters;

import bot.enums.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "job.search.filter")
public class JobSearchFilter {

	private SortBy sortBy;
	private DatePosted datePosted;
	private List<ExperienceLevel> experienceLevels = new ArrayList<>();
	private List<String> companies = new ArrayList<>();
	private List<JobType> jobTypes = new ArrayList<>();

	private List<WorkType> workTypes = new ArrayList<>();
	private EasyApplyOption easyApply;
	private List<Location> locations = new ArrayList<>();
	private List<Industry> industries = new ArrayList<>();
	private List<JobFunction> jobFunctions = new ArrayList<>();
	private List<Title> titles = new ArrayList<>();
	private Under10Applicants under10Applicants;
	private InYourNetwork inYourNetwork;
	private List<Commitment> commitments = new ArrayList<>();
	@Setter
	private String searchQuery;

	public enum Industry {
		IT_SERVICES_AND_IT_CONSULTING, SOFTWARE_DEVELOPMENT, STAFFING_AND_RECRUITING, TECHNOLOGY_INFORMATION_AND_INTERNET,
		FINANCIAL_SERVICE, TECHNOLOGY_INFORMATION_AND_MEDIA, HUMAN_RESOURCES_SERVICES, BUSINESS_CONSULTING_AND_SERVICES,
		INFORMATION_TECHNOLOGY_AND_SERVICES, BANKING, INFORMATION_SERVICES, COMPUTER_HARDWARE_MANUFACTURING,
		COMPUTER_AND_NETWORK_SECURITY, PHARMACEUTICAL_MANUFACTURING;
	}

	public enum JobFunction {
		INFORMATION_TECHNOLOGY, ENGINEERING, OTHER, MANAGEMENT, MANUFACTURING, BUSINESS_DEVELOPMENT, CONSULTING, QUALITY_ASSURANCE, DESIGN, SALES;
	}

	public enum Title {
		SOFTWARE_ENGINEER, DEVOPS_ENGINEER, CLOUD_ENGINEER, FULL_STACK_ENGINEER, INFORMATION_TECHNOLOGY_ARCHITECT, APPLICATION_DEVELOPER,
		PLATFORM_ENGINEER, SENIOR_SOFTWARE_ENGINEER, DATA_ENGINEER, JAVA_SOFTWARE_ENGINEER, SITE_RELIABILITY_ENGINEER;
	}

	public enum Commitment {
		CAREER_GROWTH_AND_LEARNING, DIVERSITY_EQUITY_AND_INCLUSION, ENVIRONMENTAL_SUSTAINABILITY, SOCIAL_IMPACT, WORK_LIFE_BALANCE;
	}
}
