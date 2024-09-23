package bot.linkedin.services;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JobsDayCountRepo extends JpaRepository<JobsDayCount, LocalDate> {

	@Modifying
	@Transactional
	@Query(value = "update JobsDayCount set count = count + 1 where date = :search")
	void incrementCount(@Param("search") LocalDate date);

	@Transactional
	default void incrementCountSafely(LocalDate date) {
		Optional<JobsDayCount> byId = findById(date);
		if (byId.isEmpty()) saveAndFlush(new JobsDayCount(date));
		incrementCount(date);
	}
}
