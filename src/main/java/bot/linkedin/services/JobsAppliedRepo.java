package bot.linkedin.services;

import bot.linkedin.models.JobsApplied;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsAppliedRepo extends JpaRepository<JobsApplied, Long> {
	boolean existsByJobDesc(String jobDesc);
}
