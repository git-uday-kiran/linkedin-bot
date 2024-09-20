package bot.linkedin.question_answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionAnswerRepo extends JpaRepository<QuestionAnswer, String> {
	Optional<QuestionAnswer> findByQuestion(String question);
}
