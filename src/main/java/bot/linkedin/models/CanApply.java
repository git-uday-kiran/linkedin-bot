package bot.linkedin.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CanApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "text(3000)")
    String jobTitle;

    @Column(nullable = false, columnDefinition = "text(6000)")
    String jobDescription;

    @Column(nullable = false, columnDefinition = "text(3000)")
    String jobUrl;

    @Column(nullable = false)
    LocalDate createdAt;

    public CanApply(String jobTitle, String jobDescription, String jobUrl) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobUrl = jobUrl;
    }

    public CanApply(Long id, String jobTitle, String jobDescription, String jobUrl, LocalDate createdAt) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobUrl = jobUrl;
        this.createdAt = createdAt;
    }

    public CanApply() {
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
    }

    public Long getId() {
        return this.id;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public String getJobDescription() {
        return this.jobDescription;
    }

    public String getJobUrl() {
        return this.jobUrl;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }
}
