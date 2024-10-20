package bot.linkedin.models;

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
public class QuestionAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false, unique = true, columnDefinition = "text(6000)")
	String question;

	@Column(nullable = false, columnDefinition = "text(6000)")
	String answer;

	@Enumerated(EnumType.STRING)
	Tag tag;

	public QuestionAnswer(String question, String answer, Tag tag) {
		this.question = question;
		this.answer = answer;
		this.tag = tag;
	}

}
