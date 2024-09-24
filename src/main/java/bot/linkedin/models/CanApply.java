package bot.linkedin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(nullable = false, columnDefinition = "text(3000)")
	String jobUrl;

	public CanApply(String jobTitle, String jobUrl) {
		this.jobTitle = jobTitle;
		this.jobUrl = jobUrl;
	}
}
