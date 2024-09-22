package bot.linkedin.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanApplyRepo extends JpaRepository<CanApply, Long> {
}
