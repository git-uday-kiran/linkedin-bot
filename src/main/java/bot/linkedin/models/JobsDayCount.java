package bot.linkedin.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class JobsDayCount {

    @Id
    LocalDate date;

    @Column(nullable = false)
    Long count = 0L;

    public JobsDayCount(LocalDate date) {
        this.date = date;
    }

    public JobsDayCount() {
    }

    public String toString() {
        return "JobsDayCount(date=" + this.date + ", count=" + this.count + ")";
    }
}
