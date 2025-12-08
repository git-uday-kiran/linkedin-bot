package bot.linkedin.filters;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("job.apply.config")
public class JobApplyConfig {

    private boolean scanJobsInHomePage = false;

    private boolean applyWithoutSearchFilter = false;

    private boolean skipViewedJobs = false;

    private int allowMaxYOE = 3;

    private List<String> jobsUrls = new ArrayList<>();

    private List<String> easyApplyJobUrls = new ArrayList<>();

    public JobApplyConfig() {
    }

    public boolean isScanJobsInHomePage() {
        return this.scanJobsInHomePage;
    }

    public boolean isApplyWithoutSearchFilter() {
        return this.applyWithoutSearchFilter;
    }

    public boolean isSkipViewedJobs() {
        return this.skipViewedJobs;
    }

    public int getAllowMaxYOE() {
        return this.allowMaxYOE;
    }

    public List<String> getJobsUrls() {
        return this.jobsUrls;
    }

    public List<String> getEasyApplyJobUrls() {
        return this.easyApplyJobUrls;
    }

    public void setScanJobsInHomePage(boolean scanJobsInHomePage) {
        this.scanJobsInHomePage = scanJobsInHomePage;
    }

    public void setApplyWithoutSearchFilter(boolean applyWithoutSearchFilter) {
        this.applyWithoutSearchFilter = applyWithoutSearchFilter;
    }

    public void setSkipViewedJobs(boolean skipViewedJobs) {
        this.skipViewedJobs = skipViewedJobs;
    }

    public void setAllowMaxYOE(int allowMaxYOE) {
        this.allowMaxYOE = allowMaxYOE;
    }

    public void setJobsUrls(List<String> jobsUrls) {
        this.jobsUrls = jobsUrls;
    }

    public void setEasyApplyJobUrls(List<String> easyApplyJobUrls) {
        this.easyApplyJobUrls = easyApplyJobUrls;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JobApplyConfig)) return false;
        final JobApplyConfig other = (JobApplyConfig) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isScanJobsInHomePage() != other.isScanJobsInHomePage()) return false;
        if (this.isApplyWithoutSearchFilter() != other.isApplyWithoutSearchFilter()) return false;
        if (this.isSkipViewedJobs() != other.isSkipViewedJobs()) return false;
        if (this.getAllowMaxYOE() != other.getAllowMaxYOE()) return false;
        final Object this$jobsUrls = this.getJobsUrls();
        final Object other$jobsUrls = other.getJobsUrls();
        if (this$jobsUrls == null ? other$jobsUrls != null : !this$jobsUrls.equals(other$jobsUrls)) return false;
        final Object this$easyApplyJobUrls = this.getEasyApplyJobUrls();
        final Object other$easyApplyJobUrls = other.getEasyApplyJobUrls();
        if (this$easyApplyJobUrls == null ? other$easyApplyJobUrls != null : !this$easyApplyJobUrls.equals(other$easyApplyJobUrls))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JobApplyConfig;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isScanJobsInHomePage() ? 79 : 97);
        result = result * PRIME + (this.isApplyWithoutSearchFilter() ? 79 : 97);
        result = result * PRIME + (this.isSkipViewedJobs() ? 79 : 97);
        result = result * PRIME + this.getAllowMaxYOE();
        final Object $jobsUrls = this.getJobsUrls();
        result = result * PRIME + ($jobsUrls == null ? 43 : $jobsUrls.hashCode());
        final Object $easyApplyJobUrls = this.getEasyApplyJobUrls();
        result = result * PRIME + ($easyApplyJobUrls == null ? 43 : $easyApplyJobUrls.hashCode());
        return result;
    }

    public String toString() {
        return "JobApplyConfig(scanJobsInHomePage=" + this.isScanJobsInHomePage() + ", applyWithoutSearchFilter=" + this.isApplyWithoutSearchFilter() + ", skipViewedJobs=" + this.isSkipViewedJobs() + ", allowMaxYOE=" + this.getAllowMaxYOE() + ", jobsUrls=" + this.getJobsUrls() + ", easyApplyJobUrls=" + this.getEasyApplyJobUrls() + ")";
    }
}
