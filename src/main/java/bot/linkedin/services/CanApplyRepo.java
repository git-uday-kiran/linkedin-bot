package bot.linkedin.services;

import bot.linkedin.models.CanApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanApplyRepo extends JpaRepository<CanApply, Long> {
}
