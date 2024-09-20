package bot.linkedin.question_answer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question_answer")
public class QuestionAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, unique = true, columnDefinition = "text(6000)")
	String question;

	@Column(nullable = false, columnDefinition = "text(6000)")
	String answer;

	public QuestionAnswer(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
}
