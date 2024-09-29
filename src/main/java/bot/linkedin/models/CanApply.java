package bot.linkedin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

	@PrePersist
	public void prePersist() {
		createdAt = LocalDate.now();
	}
}
