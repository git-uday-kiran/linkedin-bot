package bot.linkedin.filters;

import bot.enums.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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
    private String searchQuery;

    public JobSearchFilter() {
    }

    public SortBy getSortBy() {
        return this.sortBy;
    }

    public DatePosted getDatePosted() {
        return this.datePosted;
    }

    public List<ExperienceLevel> getExperienceLevels() {
        return this.experienceLevels;
    }

    public List<String> getCompanies() {
        return this.companies;
    }

    public List<JobType> getJobTypes() {
        return this.jobTypes;
    }

    public List<WorkType> getWorkTypes() {
        return this.workTypes;
    }

    public EasyApplyOption getEasyApply() {
        return this.easyApply;
    }

    public List<Location> getLocations() {
        return this.locations;
    }

    public List<Industry> getIndustries() {
        return this.industries;
    }

    public List<JobFunction> getJobFunctions() {
        return this.jobFunctions;
    }

    public List<Title> getTitles() {
        return this.titles;
    }

    public Under10Applicants getUnder10Applicants() {
        return this.under10Applicants;
    }

    public InYourNetwork getInYourNetwork() {
        return this.inYourNetwork;
    }

    public List<Commitment> getCommitments() {
        return this.commitments;
    }

    public String getSearchQuery() {
        return this.searchQuery;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void setDatePosted(DatePosted datePosted) {
        this.datePosted = datePosted;
    }

    public void setExperienceLevels(List<ExperienceLevel> experienceLevels) {
        this.experienceLevels = experienceLevels;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public void setJobTypes(List<JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public void setWorkTypes(List<WorkType> workTypes) {
        this.workTypes = workTypes;
    }

    public void setEasyApply(EasyApplyOption easyApply) {
        this.easyApply = easyApply;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setIndustries(List<Industry> industries) {
        this.industries = industries;
    }

    public void setJobFunctions(List<JobFunction> jobFunctions) {
        this.jobFunctions = jobFunctions;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    public void setUnder10Applicants(Under10Applicants under10Applicants) {
        this.under10Applicants = under10Applicants;
    }

    public void setInYourNetwork(InYourNetwork inYourNetwork) {
        this.inYourNetwork = inYourNetwork;
    }

    public void setCommitments(List<Commitment> commitments) {
        this.commitments = commitments;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
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

    public enum Commitment {
        CAREER_GROWTH_AND_LEARNING, DIVERSITY_EQUITY_AND_INCLUSION, ENVIRONMENTAL_SUSTAINABILITY, SOCIAL_IMPACT, WORK_LIFE_BALANCE;
    }
}
