package bot.linkedin.services;

import bot.linkedin.models.QuestionAnswer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionAnswerRepo extends JpaRepository<QuestionAnswer, Long> {

	Optional<QuestionAnswer> findByQuestion(String question);

	@Transactional
	void deleteByQuestion(String question);

}
