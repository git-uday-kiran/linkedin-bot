package bot.linkedin.services;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	String jobTitle;

	String jobUrl;

	public CanApply(String jobTitle, String jobUrl) {
		this.jobTitle = jobTitle;
		this.jobUrl = jobUrl;
	}
}
