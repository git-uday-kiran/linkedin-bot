package bot.linkedin;

import bot.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "job.filter")
public class JobFilter {

	private final SortBy sortBy;
	private final DatePosted datePosted;
	private final List<ExperienceLevel> experienceLevels;
	private final List<String> companies;
	private final List<JobType> jobTypes;

	private final List<Remote> remotes;
	private final EasyApplyOption easyApply;
	private final List<Location> locations;
	private final List<Industry> industries;
	private final List<JobFunction> jobFunctions;
	private final List<Title> titles;
	private final Under10Applicants under10Applicants;
	private final InYourNetwork inYourNetwork;
	private final List<Commitment> commitments;
	@Setter
	private String searchQuery;

	public enum Location {
		BENGALURU, HYDERABAD, PUNE, CHENNAI, BENGALURU_EAST, NOIDA, GURUGRAM, GURGAON, MUMBAI, AHMEDABAD, TRIVANDRUM;
	}

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

	public enum InYourNetwork {
		ENABLE, DISABLE;
	}

	public enum Commitment {
		CAREER_GROWTH_AND_LEARNING, DIVERSITY_EQUITY_AND_INCLUSION, ENVIRONMENTAL_SUSTAINABILITY, SOCIAL_IMPACT, WORK_LIFE_BALANCE;
	}
}
