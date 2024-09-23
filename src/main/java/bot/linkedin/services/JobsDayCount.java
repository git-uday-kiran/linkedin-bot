package bot.linkedin.services;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@ToString
@NoArgsConstructor
public class JobsDayCount {

	@Id
	LocalDate date;

	@Column(nullable = false)
	Long count = 0L;

	public JobsDayCount(LocalDate date) {
		this.date = date;
	}
}
