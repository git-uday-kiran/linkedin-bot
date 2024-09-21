package bot.linkedin.services;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class JobsApplied {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, unique = true, columnDefinition = "text(6000)")
	String jobDesc;

	public JobsApplied(String jobDesc) {
		this.jobDesc = jobDesc;
	}
}
