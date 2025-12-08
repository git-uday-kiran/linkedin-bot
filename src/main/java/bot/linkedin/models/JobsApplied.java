package bot.linkedin.models;

import jakarta.persistence.*;

@Entity
public class JobsApplied {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = false, columnDefinition = "text(6000)")
    String jobDesc;

    public JobsApplied(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public JobsApplied() {
    }

    public Long getId() {
        return this.id;
    }

    public String getJobDesc() {
        return this.jobDesc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }
}
