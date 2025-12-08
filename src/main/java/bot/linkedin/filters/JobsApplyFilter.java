package bot.linkedin.filters;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "job.apply.filter")
public class JobsApplyFilter {

    private JobTitleFilter jobTitle;
    private JobDescFilter jobDesc;

    public JobsApplyFilter() {
    }

    public JobTitleFilter getJobTitle() {
        return this.jobTitle;
    }

    public JobDescFilter getJobDesc() {
        return this.jobDesc;
    }

    public void setJobTitle(JobTitleFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobDesc(JobDescFilter jobDesc) {
        this.jobDesc = jobDesc;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JobsApplyFilter)) return false;
        final JobsApplyFilter other = (JobsApplyFilter) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$jobTitle = this.getJobTitle();
        final Object other$jobTitle = other.getJobTitle();
        if (this$jobTitle == null ? other$jobTitle != null : !this$jobTitle.equals(other$jobTitle)) return false;
        final Object this$jobDesc = this.getJobDesc();
        final Object other$jobDesc = other.getJobDesc();
        if (this$jobDesc == null ? other$jobDesc != null : !this$jobDesc.equals(other$jobDesc)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JobsApplyFilter;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $jobTitle = this.getJobTitle();
        result = result * PRIME + ($jobTitle == null ? 43 : $jobTitle.hashCode());
        final Object $jobDesc = this.getJobDesc();
        result = result * PRIME + ($jobDesc == null ? 43 : $jobDesc.hashCode());
        return result;
    }

    public String toString() {
        return "JobsApplyFilter(jobTitle=" + this.getJobTitle() + ", jobDesc=" + this.getJobDesc() + ")";
    }


    public static class JobDescFilter {
        private Set<String> excludeWords;
        private Set<String> includeWords;
        private Set<String> mandatoryWords;

        public JobDescFilter() {
        }

        public Set<String> getExcludeWords() {
            return this.excludeWords;
        }

        public Set<String> getIncludeWords() {
            return this.includeWords;
        }

        public Set<String> getMandatoryWords() {
            return this.mandatoryWords;
        }

        public void setExcludeWords(Set<String> excludeWords) {
            this.excludeWords = excludeWords;
        }

        public void setIncludeWords(Set<String> includeWords) {
            this.includeWords = includeWords;
        }

        public void setMandatoryWords(Set<String> mandatoryWords) {
            this.mandatoryWords = mandatoryWords;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof JobDescFilter)) return false;
            final JobDescFilter other = (JobDescFilter) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$excludeWords = this.getExcludeWords();
            final Object other$excludeWords = other.getExcludeWords();
            if (this$excludeWords == null ? other$excludeWords != null : !this$excludeWords.equals(other$excludeWords))
                return false;
            final Object this$includeWords = this.getIncludeWords();
            final Object other$includeWords = other.getIncludeWords();
            if (this$includeWords == null ? other$includeWords != null : !this$includeWords.equals(other$includeWords))
                return false;
            final Object this$mandatoryWords = this.getMandatoryWords();
            final Object other$mandatoryWords = other.getMandatoryWords();
            if (this$mandatoryWords == null ? other$mandatoryWords != null : !this$mandatoryWords.equals(other$mandatoryWords))
                return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof JobDescFilter;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $excludeWords = this.getExcludeWords();
            result = result * PRIME + ($excludeWords == null ? 43 : $excludeWords.hashCode());
            final Object $includeWords = this.getIncludeWords();
            result = result * PRIME + ($includeWords == null ? 43 : $includeWords.hashCode());
            final Object $mandatoryWords = this.getMandatoryWords();
            result = result * PRIME + ($mandatoryWords == null ? 43 : $mandatoryWords.hashCode());
            return result;
        }

        public String toString() {
            return "JobsApplyFilter.JobDescFilter(excludeWords=" + this.getExcludeWords() + ", includeWords=" + this.getIncludeWords() + ", mandatoryWords=" + this.getMandatoryWords() + ")";
        }
    }

    public static class JobTitleFilter {
        private Set<String> includeWords;
        private Set<String> excludeWords;
        private Set<String> mandatoryWords;

        public JobTitleFilter() {
        }

        public Set<String> getIncludeWords() {
            return this.includeWords;
        }

        public Set<String> getExcludeWords() {
            return this.excludeWords;
        }

        public Set<String> getMandatoryWords() {
            return this.mandatoryWords;
        }

        public void setIncludeWords(Set<String> includeWords) {
            this.includeWords = includeWords;
        }

        public void setExcludeWords(Set<String> excludeWords) {
            this.excludeWords = excludeWords;
        }

        public void setMandatoryWords(Set<String> mandatoryWords) {
            this.mandatoryWords = mandatoryWords;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof JobTitleFilter)) return false;
            final JobTitleFilter other = (JobTitleFilter) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$includeWords = this.getIncludeWords();
            final Object other$includeWords = other.getIncludeWords();
            if (this$includeWords == null ? other$includeWords != null : !this$includeWords.equals(other$includeWords))
                return false;
            final Object this$excludeWords = this.getExcludeWords();
            final Object other$excludeWords = other.getExcludeWords();
            if (this$excludeWords == null ? other$excludeWords != null : !this$excludeWords.equals(other$excludeWords))
                return false;
            final Object this$mandatoryWords = this.getMandatoryWords();
            final Object other$mandatoryWords = other.getMandatoryWords();
            if (this$mandatoryWords == null ? other$mandatoryWords != null : !this$mandatoryWords.equals(other$mandatoryWords))
                return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof JobTitleFilter;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $includeWords = this.getIncludeWords();
            result = result * PRIME + ($includeWords == null ? 43 : $includeWords.hashCode());
            final Object $excludeWords = this.getExcludeWords();
            result = result * PRIME + ($excludeWords == null ? 43 : $excludeWords.hashCode());
            final Object $mandatoryWords = this.getMandatoryWords();
            result = result * PRIME + ($mandatoryWords == null ? 43 : $mandatoryWords.hashCode());
            return result;
        }

        public String toString() {
            return "JobsApplyFilter.JobTitleFilter(includeWords=" + this.getIncludeWords() + ", excludeWords=" + this.getExcludeWords() + ", mandatoryWords=" + this.getMandatoryWords() + ")";
        }
    }

}
